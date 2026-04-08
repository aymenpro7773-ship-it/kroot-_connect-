package com.kroot.connect.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.kroot.connect.MainActivity;
import com.kroot.connect.R;

/**
 * فئة مساعدة لإدارة الإشعارات
 */
public class NotificationHelper {

    private static final String CHANNEL_ID_SUCCESS = "kroot_success";
    private static final String CHANNEL_ID_ERROR = "kroot_error";
    private static final String CHANNEL_ID_WARNING = "kroot_warning";
    private static final int NOTIFICATION_ID_SUCCESS = 1;
    private static final int NOTIFICATION_ID_ERROR = 2;
    private static final int NOTIFICATION_ID_WARNING = 3;

    /**
     * إنشاء قنوات الإشعارات
     */
    public static void createNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // قناة النجاح
            NotificationChannel successChannel = new NotificationChannel(
                CHANNEL_ID_SUCCESS,
                "إشعارات النجاح",
                NotificationManager.IMPORTANCE_DEFAULT
            );
            successChannel.setDescription("إشعارات العمليات الناجحة");
            notificationManager.createNotificationChannel(successChannel);

            // قناة الأخطاء
            NotificationChannel errorChannel = new NotificationChannel(
                CHANNEL_ID_ERROR,
                "إشعارات الأخطاء",
                NotificationManager.IMPORTANCE_HIGH
            );
            errorChannel.setDescription("إشعارات الأخطاء والمشاكل");
            notificationManager.createNotificationChannel(errorChannel);

            // قناة التحذيرات
            NotificationChannel warningChannel = new NotificationChannel(
                CHANNEL_ID_WARNING,
                "إشعارات التحذير",
                NotificationManager.IMPORTANCE_DEFAULT
            );
            warningChannel.setDescription("إشعارات التحذير والتنبيهات");
            notificationManager.createNotificationChannel(warningChannel);
        }
    }

    /**
     * عرض إشعار عام
     */
    public static void showNotification(Context context, String title, String message) {
        PreferencesManager prefs = PreferencesManager.getInstance(context);
        if (!prefs.isNotificationsEnabled()) {
            return;
        }

        NotificationManager notificationManager =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // تحديد نوع الإشعار بناءً على العنوان
        String channelId = CHANNEL_ID_SUCCESS;
        int notificationId = NOTIFICATION_ID_SUCCESS;

        if (title.contains("خطأ") || title.contains("فشل")) {
            channelId = CHANNEL_ID_ERROR;
            notificationId = NOTIFICATION_ID_ERROR;
        } else if (title.contains("تحذير")) {
            channelId = CHANNEL_ID_WARNING;
            notificationId = NOTIFICATION_ID_WARNING;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // إضافة الصوت والاهتزاز إذا كانا مفعلين
        if (prefs.isNotificationSoundEnabled()) {
            builder.setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI);
        }

        if (prefs.isNotificationVibrationEnabled()) {
            builder.setVibrate(new long[]{0, 500, 250, 500});
        }

        Notification notification = builder.build();
        notificationManager.notify(notificationId, notification);
    }

    /**
     * عرض إشعار النجاح
     */
    public static void showSuccessNotification(Context context, String message) {
        showNotification(context, "عملية ناجحة", message);
    }

    /**
     * عرض إشعار الخطأ
     */
    public static void showErrorNotification(Context context, String message) {
        showNotification(context, "خطأ", message);
    }

    /**
     * عرض إشعار التحذير
     */
    public static void showWarningNotification(Context context, String message) {
        showNotification(context, "تحذير", message);
    }

    /**
     * إلغاء الإشعار
     */
    public static void cancelNotification(Context context, int notificationId) {
        NotificationManager notificationManager =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
    }

    /**
     * إلغاء جميع الإشعارات
     */
    public static void cancelAllNotifications(Context context) {
        NotificationManager notificationManager =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
