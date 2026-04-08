package com.kroot.connect.ui;

import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kroot.connect.R;
import com.kroot.connect.database.AppDatabase;
import com.kroot.connect.database.entity.TransactionLogEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * نشاط السجلات - عرض سجل جميع العمليات
 */
public class LogsActivity extends AppCompatActivity {

    private RecyclerView logsRecyclerView;
    private Spinner filterSpinner;
    private TextView noLogsText;
    private AppDatabase database;
    private LogsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        database = AppDatabase.getInstance(this);

        initializeViews();
        loadLogs();
    }

    /**
     * تهيئة عناصر الواجهة
     */
    private void initializeViews() {
        // logsRecyclerView = findViewById(R.id.logs_recycler_view);
        // filterSpinner = findViewById(R.id.filter_spinner);
        // noLogsText = findViewById(R.id.no_logs_text);

        // logsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // adapter = new LogsAdapter(new ArrayList<>());
        // logsRecyclerView.setAdapter(adapter);

        // filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        //     @Override
        //     public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //         filterLogs(position);
        //     }
        //
        //     @Override
        //     public void onNothingSelected(AdapterView<?> parent) {
        //     }
        // });
    }

    /**
     * تحميل السجلات
     */
    private void loadLogs() {
        // database.transactionLogDao().getAllLogs().observe(this, logs -> {
        //     if (logs == null || logs.isEmpty()) {
        //         noLogsText.setVisibility(View.VISIBLE);
        //         logsRecyclerView.setVisibility(View.GONE);
        //     } else {
        //         noLogsText.setVisibility(View.GONE);
        //         logsRecyclerView.setVisibility(View.VISIBLE);
        //         adapter.setLogs(logs);
        //     }
        // });
    }

    /**
     * تصفية السجلات
     */
    private void filterLogs(int filterType) {
        // switch (filterType) {
        //     case 0: // الكل
        //         loadLogs();
        //         break;
        //     case 1: // نجح
        //         database.transactionLogDao().getLogsByStatus("success").observe(this, logs -> {
        //             adapter.setLogs(logs != null ? logs : new ArrayList<>());
        //         });
        //         break;
        //     case 2: // فشل
        //         database.transactionLogDao().getLogsByStatus("failed").observe(this, logs -> {
        //             adapter.setLogs(logs != null ? logs : new ArrayList<>());
        //         });
        //         break;
        // }
    }

    /**
     * محول قائمة السجلات
     */
    public static class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.LogViewHolder> {

        private List<TransactionLogEntity> logs;
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

        public LogsAdapter(List<TransactionLogEntity> logs) {
            this.logs = logs;
        }

        public void setLogs(List<TransactionLogEntity> logs) {
            this.logs = logs;
            notifyDataSetChanged();
        }

        @Override
        public LogViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            android.view.View view = android.view.LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_log, parent, false);
            return new LogViewHolder(view);
        }

        @Override
        public void onBindViewHolder(LogViewHolder holder, int position) {
            TransactionLogEntity log = logs.get(position);
            holder.bind(log, dateFormat);
        }

        @Override
        public int getItemCount() {
            return logs.size();
        }

        public static class LogViewHolder extends RecyclerView.ViewHolder {

            private final TextView timeText;
            private final TextView amountText;
            private final TextView numberText;
            private final TextView statusText;

            public LogViewHolder(android.view.View itemView) {
                super(itemView);
                // timeText = itemView.findViewById(R.id.log_time);
                // amountText = itemView.findViewById(R.id.log_amount);
                // numberText = itemView.findViewById(R.id.log_number);
                // statusText = itemView.findViewById(R.id.log_status);
                
                timeText = null;
                amountText = null;
                numberText = null;
                statusText = null;
            }

            public void bind(TransactionLogEntity log, SimpleDateFormat dateFormat) {
                if (timeText != null) {
                    timeText.setText(dateFormat.format(new Date(log.getTimestamp())));
                }
                if (amountText != null) {
                    amountText.setText(log.getAmount() + " ر.ي");
                }
                if (numberText != null) {
                    numberText.setText(log.getCustomerNumber());
                }
                if (statusText != null) {
                    statusText.setText(log.getStatus());
                    int color = log.getStatus().equals("success") ? 
                        itemView.getContext().getColor(R.color.success) :
                        itemView.getContext().getColor(R.color.error);
                    statusText.setTextColor(color);
                }
            }
        }
    }
}
