package com.kroot.connect.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.Nullable;

import com.kroot.connect.database.AppDatabase;
import com.kroot.connect.database.entity.CardEntity;
import com.kroot.connect.database.entity.TransactionLogEntity;
import com.kroot.connect.utils.NotificationHelper;
import com.kroot.connect.utils.PreferencesManager;
import com.kroot.connect.utils.SmsMessageParser;

/**
 * خدمة معالجة رسائل SMS وإرسال الكروت تلقائياً
 */
public class SmsProcessingService extends Service {

    private static final String TAG = "SmsProcessingService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            stopSelf();
            return START_NOT_STICKY;
        }

        // استخراج البيانات من Intent
        String senderAddress = intent.getStringExtra("sender_address");
        String messageBody = intent.getStringExtra("message_body");
        long timestamp = intent.getLongExtra("timestamp", System.currentTimeMillis());

        // معالجة الرسالة في خيط منفصل
        new Thread(() -> processSms(senderAddress, messageBody, timestamp)).start();

        return START_NOT_STICKY;
    }

    /**
     * معالجة الرسالة واستخراج البيانات وإرسال الكرت
     */
    private void processSms(String senderAddress, String messageBody, long timestamp) {
        try {
            PreferencesManager prefs = PreferencesManager.getInstance(this);
            String expectedSender = prefs.getSenderName();

            // تحليل الرسالة
            SmsMessageParser.SmsAnalysisResult analysisResult = 
                SmsMessageParser.analyzeSms(messageBody, senderAddress, expectedSender);

            Log.d(TAG, "نتيجة التحليل: " + analysisResult);

            // إنشاء سجل العملية
            TransactionLogEntity log = new TransactionLogEntity(
                timestamp,
                analysisResult.amount != null ? analysisResult.amount : 0,
                analysisResult.customerNumber,
                "",
                "pending"
            );
            log.setOriginalMessage(messageBody);

            AppDatabase db = AppDatabase.getInstance(this);

            if (!analysisResult.isValid) {
                log.setStatus("failed");
                log.setErrorMessage(analysisResult.reason);
                db.transactionLogDao().insertLog(log);
                Log.w(TAG, "فشل التحليل: " + analysisResult.reason);
                return;
            }

            // البحث عن كرت متاح من الفئة المطلوبة
            CardEntity availableCard = db.cardDao()
                .getAvailableCardByCategory(analysisResult.amount);

            if (availableCard == null) {
                log.setStatus("failed");
                log.setErrorMessage("لا توجد كروت متاحة في هذه الفئة");
                db.transactionLogDao().insertLog(log);
                
                NotificationHelper.showNotification(
                    this,
                    "خطأ",
                    "لا توجد كروت متاحة في فئة " + analysisResult.amount
                );
                return;
            }

            // إرسال الكرت عبر SMS
            boolean smsSent = sendCardViaSms(
                analysisResult.customerNumber,
                availableCard.getCardCode()
            );

            if (smsSent) {
                // تحديث حالة الكرت إلى "مستخدم"
                availableCard.setStatus("used");
                availableCard.setUsedAt(System.currentTimeMillis());
                availableCard.setUsedByNumber(analysisResult.customerNumber);
                db.cardDao().updateCard(availableCard);

                // تحديث سجل العملية
                log.setStatus("success");
                log.setCardCode(availableCard.getCardCode());
                db.transactionLogDao().insertLog(log);

                // إرسال إشعار النجاح
                NotificationHelper.showNotification(
                    this,
                    "عملية ناجحة",
                    "تم إرسال الكرت للعميل: " + analysisResult.customerNumber
                );

                Log.d(TAG, "تم إرسال الكرت بنجاح");
            } else {
                log.setStatus("failed");
                log.setErrorMessage("فشل إرسال الرسالة النصية");
                db.transactionLogDao().insertLog(log);

                NotificationHelper.showNotification(
                    this,
                    "خطأ",
                    "فشل إرسال الكرت للعميل"
                );
            }

            // التحقق من مستوى المخزون
            checkLowStockAlert(db, analysisResult.amount);

        } catch (Exception e) {
            Log.e(TAG, "خطأ في معالجة الرسالة: " + e.getMessage());
            e.printStackTrace();
        } finally {
            stopSelf();
        }
    }

    /**
     * إرسال الكرت عبر SMS
     */
    private boolean sendCardViaSms(String phoneNumber, String cardCode) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, cardCode, null, null);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "خطأ في إرسال الرسالة: " + e.getMessage());
            return false;
        }
    }

    /**
     * التحقق من مستوى المخزون وإرسال تنبيه إذا لزم الأمر
     */
    private void checkLowStockAlert(AppDatabase db, int category) {
        try {
            int threshold = PreferencesManager.getInstance(this).getLowStockThreshold();
            int availableCount = db.cardDao()
                .getAvailableCardsCount(category).getValue();

            if (availableCount != null && availableCount < threshold) {
                NotificationHelper.showNotification(
                    this,
                    "تحذير المخزون",
                    "عدد الكروت المتبقية في فئة " + category + " أقل من " + threshold
                );
            }
        } catch (Exception e) {
            Log.e(TAG, "خطأ في التحقق من المخزون: " + e.getMessage());
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
