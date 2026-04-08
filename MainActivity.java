package com.kroot.connect;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kroot.connect.databinding.ActivityMainBinding;
import com.kroot.connect.ui.DashboardActivity;
import com.kroot.connect.utils.NotificationHelper;

/**
 * النشاط الرئيسي للتطبيق
 */
public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // إنشاء قنوات الإشعارات
        NotificationHelper.createNotificationChannels(this);

        // طلب الأذونات المطلوبة
        requestRequiredPermissions();

        // إعداد التنقل السفلي
        setupBottomNavigation();
    }

    /**
     * طلب الأذونات المطلوبة
     */
    private void requestRequiredPermissions() {
        String[] permissions = {
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_CONTACTS
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.POST_NOTIFICATIONS
            };
        }

        boolean needsPermission = false;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                needsPermission = true;
                break;
            }
        }

        if (needsPermission) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * إعداد التنقل السفلي
     */
    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = binding.bottomNavigation;

        bottomNav.setOnItemSelectedListener(item -> {
            Intent intent = null;

            if (item.getItemId() == R.id.nav_dashboard) {
                intent = new Intent(this, DashboardActivity.class);
            } else if (item.getItemId() == R.id.nav_cards) {
                // intent = new Intent(this, CardsManagementActivity.class);
            } else if (item.getItemId() == R.id.nav_logs) {
                // intent = new Intent(this, LogsActivity.class);
            } else if (item.getItemId() == R.id.nav_settings) {
                // intent = new Intent(this, SettingsActivity.class);
            }

            if (intent != null) {
                startActivity(intent);
            }

            return true;
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (!allPermissionsGranted) {
                NotificationHelper.showWarningNotification(
                    this,
                    "يتطلب التطبيق جميع الأذونات للعمل بشكل صحيح"
                );
            }
        }
    }
}
