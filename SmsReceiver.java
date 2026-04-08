package com.kroot.connect.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.kroot.connect.services.SmsProcessingService;
import com.kroot.connect.utils.PreferencesManager;

/**
 * مستقبل البث لاستقبال رسائل SMS الواردة
 */
public class SmsReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        // التحقق من أن الأتمتة مفعلة
        PreferencesManager prefs = PreferencesManager.getInstance(context);
        if (!prefs.isAutomationEnabled()) {
            Log.d(TAG, "الأتمتة معطلة");
            return;
        }

        // استخراج الرسائل من Bundle
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }

        try {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus == null) {
                return;
            }

            for (Object pdu : pdus) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                String senderAddress = smsMessage.getOriginatingAddress();
                String messageBody = smsMessage.getMessageBody();
                long timestamp = System.currentTimeMillis();

                Log.d(TAG, "رسالة مستقبلة من: " + senderAddress);
                Log.d(TAG, "محتوى الرسالة: " + messageBody);

                // إرسال الرسالة إلى خدمة المعالجة
                Intent processingIntent = new Intent(context, SmsProcessingService.class);
                processingIntent.putExtra("sender_address", senderAddress);
                processingIntent.putExtra("message_body", messageBody);
                processingIntent.putExtra("timestamp", timestamp);
                context.startService(processingIntent);
            }

            // منع الرسالة من الظهور في تطبيق الرسائل الافتراضي
            abortBroadcast();

        } catch (Exception e) {
            Log.e(TAG, "خطأ في معالجة الرسالة: " + e.getMessage());
        }
    }
}
