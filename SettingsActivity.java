package com.kroot.connect.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.kroot.connect.R;
import com.kroot.connect.utils.NotificationHelper;
import com.kroot.connect.utils.PreferencesManager;

/**
 * نشاط الإعدادات - إدارة إعدادات التطبيق
 */
public class SettingsActivity extends AppCompatActivity {

    private EditText senderNameInput;
    private EditText responseMessageInput;
    private EditText lowStockThresholdInput;
    private Switch notificationsSwitch;
    private Switch notificationSoundSwitch;
    private Switch notificationVibrationSwitch;
    private MaterialButton saveButton;
    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferencesManager = PreferencesManager.getInstance(this);

        initializeViews();
        loadSettings();
        setupListeners();
    }

    /**
     * تهيئة عناصر الواجهة
     */
    private void initializeViews() {
        // senderNameInput = findViewById(R.id.sender_name_input);
        // responseMessageInput = findViewById(R.id.response_message_input);
        // lowStockThresholdInput = findViewById(R.id.low_stock_threshold_input);
        // notificationsSwitch = findViewById(R.id.notifications_switch);
        // notificationSoundSwitch = findViewById(R.id.notification_sound_switch);
        // notificationVibrationSwitch = findViewById(R.id.notification_vibration_switch);
        // saveButton = findViewById(R.id.save_button);
    }

    /**
     * تحميل الإعدادات الحالية
     */
    private void loadSettings() {
        // senderNameInput.setText(preferencesManager.getSenderName());
        // responseMessageInput.setText(preferencesManager.getResponseMessage());
        // lowStockThresholdInput.setText(String.valueOf(preferencesManager.getLowStockThreshold()));
        // notificationsSwitch.setChecked(preferencesManager.isNotificationsEnabled());
        // notificationSoundSwitch.setChecked(preferencesManager.isNotificationSoundEnabled());
        // notificationVibrationSwitch.setChecked(preferencesManager.isNotificationVibrationEnabled());
    }

    /**
     * إعداد المستمعين
     */
    private void setupListeners() {
        // saveButton.setOnClickListener(v -> saveSettings());
    }

    /**
     * حفظ الإعدادات
     */
    private void saveSettings() {
        try {
            String senderName = senderNameInput.getText().toString().trim();
            String responseMessage = responseMessageInput.getText().toString().trim();
            String thresholdStr = lowStockThresholdInput.getText().toString().trim();

            if (senderName.isEmpty()) {
                Toast.makeText(this, "الرجاء إدخال اسم المرسل", Toast.LENGTH_SHORT).show();
                return;
            }

            int threshold = 5;
            if (!thresholdStr.isEmpty()) {
                try {
                    threshold = Integer.parseInt(thresholdStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "حد التنبيه يجب أن يكون رقماً", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // حفظ الإعدادات
            preferencesManager.setSenderName(senderName);
            preferencesManager.setResponseMessage(responseMessage);
            preferencesManager.setLowStockThreshold(threshold);
            preferencesManager.setNotificationsEnabled(notificationsSwitch.isChecked());
            preferencesManager.setNotificationSoundEnabled(notificationSoundSwitch.isChecked());
            preferencesManager.setNotificationVibrationEnabled(notificationVibrationSwitch.isChecked());

            Toast.makeText(this, "تم حفظ الإعدادات بنجاح", Toast.LENGTH_SHORT).show();
            NotificationHelper.showSuccessNotification(this, "تم حفظ الإعدادات");

        } catch (Exception e) {
            Toast.makeText(this, "خطأ: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
