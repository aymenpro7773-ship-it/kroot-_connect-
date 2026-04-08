package com.kroot.connect.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * فئة لإدارة الإعدادات والتفضيلات المحفوظة محلياً
 */
public class PreferencesManager {

    private static final String PREFS_NAME = "KrootConnectPrefs";
    private static volatile PreferencesManager instance;
    private final SharedPreferences sharedPreferences;

    // مفاتيح الإعدادات
    private static final String KEY_AUTOMATION_ENABLED = "automation_enabled";
    private static final String KEY_SENDER_NAME = "sender_name";
    private static final String KEY_RESPONSE_MESSAGE = "response_message";
    private static final String KEY_LOW_STOCK_THRESHOLD = "low_stock_threshold";
    private static final String KEY_NOTIFICATIONS_ENABLED = "notifications_enabled";
    private static final String KEY_NOTIFICATION_SOUND = "notification_sound";
    private static final String KEY_NOTIFICATION_VIBRATION = "notification_vibration";

    private PreferencesManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * الحصول على مثيل المدير (Singleton)
     */
    public static PreferencesManager getInstance(Context context) {
        if (instance == null) {
            synchronized (PreferencesManager.class) {
                if (instance == null) {
                    instance = new PreferencesManager(context);
                }
            }
        }
        return instance;
    }

    /**
     * التحقق من تفعيل الأتمتة
     */
    public boolean isAutomationEnabled() {
        return sharedPreferences.getBoolean(KEY_AUTOMATION_ENABLED, false);
    }

    /**
     * تعيين حالة الأتمتة
     */
    public void setAutomationEnabled(boolean enabled) {
        sharedPreferences.edit().putBoolean(KEY_AUTOMATION_ENABLED, enabled).apply();
    }

    /**
     * الحصول على اسم المرسل المتوقع
     */
    public String getSenderName() {
        return sharedPreferences.getString(KEY_SENDER_NAME, "jaib");
    }

    /**
     * تعيين اسم المرسل
     */
    public void setSenderName(String senderName) {
        sharedPreferences.edit().putString(KEY_SENDER_NAME, senderName).apply();
    }

    /**
     * الحصول على رسالة الرد
     */
    public String getResponseMessage() {
        return sharedPreferences.getString(KEY_RESPONSE_MESSAGE, "");
    }

    /**
     * تعيين رسالة الرد
     */
    public void setResponseMessage(String message) {
        sharedPreferences.edit().putString(KEY_RESPONSE_MESSAGE, message).apply();
    }

    /**
     * الحصول على حد التنبيه المنخفض
     */
    public int getLowStockThreshold() {
        return sharedPreferences.getInt(KEY_LOW_STOCK_THRESHOLD, 5);
    }

    /**
     * تعيين حد التنبيه المنخفض
     */
    public void setLowStockThreshold(int threshold) {
        sharedPreferences.edit().putInt(KEY_LOW_STOCK_THRESHOLD, threshold).apply();
    }

    /**
     * التحقق من تفعيل الإشعارات
     */
    public boolean isNotificationsEnabled() {
        return sharedPreferences.getBoolean(KEY_NOTIFICATIONS_ENABLED, true);
    }

    /**
     * تعيين حالة الإشعارات
     */
    public void setNotificationsEnabled(boolean enabled) {
        sharedPreferences.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled).apply();
    }

    /**
     * التحقق من تفعيل صوت الإشعار
     */
    public boolean isNotificationSoundEnabled() {
        return sharedPreferences.getBoolean(KEY_NOTIFICATION_SOUND, true);
    }

    /**
     * تعيين حالة صوت الإشعار
     */
    public void setNotificationSoundEnabled(boolean enabled) {
        sharedPreferences.edit().putBoolean(KEY_NOTIFICATION_SOUND, enabled).apply();
    }

    /**
     * التحقق من تفعيل اهتزاز الإشعار
     */
    public boolean isNotificationVibrationEnabled() {
        return sharedPreferences.getBoolean(KEY_NOTIFICATION_VIBRATION, true);
    }

    /**
     * تعيين حالة اهتزاز الإشعار
     */
    public void setNotificationVibrationEnabled(boolean enabled) {
        sharedPreferences.edit().putBoolean(KEY_NOTIFICATION_VIBRATION, enabled).apply();
    }

    /**
     * حذف جميع الإعدادات
     */
    public void clearAllPreferences() {
        sharedPreferences.edit().clear().apply();
    }
}
