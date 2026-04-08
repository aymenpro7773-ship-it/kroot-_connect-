package com.kroot.connect.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.kroot.connect.R;
import com.kroot.connect.database.AppDatabase;
import com.kroot.connect.utils.PreferencesManager;

/**
 * نشاط لوحة التحكم - يعرض حالة الأتمتة والإحصائيات
 */
public class DashboardActivity extends AppCompatActivity {

    private Switch automationSwitch;
    private TextView dailySalesText;
    private TextView monthlySalesText;
    private TextView totalTransactionsText;
    private TextView successfulTransactionsText;
    private TextView failedTransactionsText;
    private PreferencesManager preferencesManager;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // تهيئة المتغيرات
        preferencesManager = PreferencesManager.getInstance(this);
        database = AppDatabase.getInstance(this);

        // ربط العناصر من الواجهة
        initializeViews();

        // تحديث البيانات
        updateDashboard();
    }

    /**
     * تهيئة عناصر الواجهة
     */
    private void initializeViews() {
        // automationSwitch = findViewById(R.id.automation_switch);
        // dailySalesText = findViewById(R.id.daily_sales_text);
        // monthlySalesText = findViewById(R.id.monthly_sales_text);
        // totalTransactionsText = findViewById(R.id.total_transactions_text);
        // successfulTransactionsText = findViewById(R.id.successful_transactions_text);
        // failedTransactionsText = findViewById(R.id.failed_transactions_text);

        // automationSwitch.setChecked(preferencesManager.isAutomationEnabled());
        // automationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
        //     preferencesManager.setAutomationEnabled(isChecked);
        // });
    }

    /**
     * تحديث بيانات لوحة التحكم
     */
    private void updateDashboard() {
        // تحديث الإحصائيات من قاعدة البيانات
        long todayStart = getTodayStartTime();
        long monthStart = getMonthStartTime();

        // database.transactionLogDao().getDailySales(todayStart).observe(this, sales -> {
        //     if (sales != null) {
        //         dailySalesText.setText(String.format("%.0f ر.ي", (double) sales));
        //     }
        // });

        // database.transactionLogDao().getMonthlySales(monthStart).observe(this, sales -> {
        //     if (sales != null) {
        //         monthlySalesText.setText(String.format("%.0f ر.ي", (double) sales));
        //     }
        // });

        // database.transactionLogDao().getTotalTransactions().observe(this, total -> {
        //     if (total != null) {
        //         totalTransactionsText.setText(String.valueOf(total));
        //     }
        // });

        // database.transactionLogDao().getSuccessfulTransactions().observe(this, successful -> {
        //     if (successful != null) {
        //         successfulTransactionsText.setText(String.valueOf(successful));
        //     }
        // });

        // database.transactionLogDao().getFailedTransactions().observe(this, failed -> {
        //     if (failed != null) {
        //         failedTransactionsText.setText(String.valueOf(failed));
        //     }
        // });
    }

    /**
     * الحصول على بداية اليوم الحالي
     */
    private long getTodayStartTime() {
        long now = System.currentTimeMillis();
        long dayInMillis = 24 * 60 * 60 * 1000;
        return now - (now % dayInMillis);
    }

    /**
     * الحصول على بداية الشهر الحالي
     */
    private long getMonthStartTime() {
        long now = System.currentTimeMillis();
        long dayInMillis = 24 * 60 * 60 * 1000;
        long monthInMillis = 30 * dayInMillis; // تقريبي
        return now - (now % monthInMillis);
    }
}
