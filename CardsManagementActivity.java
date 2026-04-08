package com.kroot.connect.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kroot.connect.R;
import com.kroot.connect.database.AppDatabase;
import com.kroot.connect.database.entity.CardEntity;
import com.kroot.connect.utils.NotificationHelper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * نشاط إدارة الكروت - إضافة واستيراد الكروت
 */
public class CardsManagementActivity extends AppCompatActivity {

    private EditText cardsInput;
    private Spinner categorySpinner;
    private Button addButton, importButton;
    private RecyclerView cardsRecyclerView;
    private AppDatabase database;

    private static final int PICK_FILE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_management);

        database = AppDatabase.getInstance(this);

        initializeViews();
        setupListeners();
    }

    /**
     * تهيئة عناصر الواجهة
     */
    private void initializeViews() {
        // cardsInput = findViewById(R.id.cards_input);
        // categorySpinner = findViewById(R.id.category_spinner);
        // addButton = findViewById(R.id.add_button);
        // importButton = findViewById(R.id.import_button);
        // cardsRecyclerView = findViewById(R.id.cards_recycler_view);

        // cardsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * إعداد المستمعين
     */
    private void setupListeners() {
        // addButton.setOnClickListener(v -> addCards());
        // importButton.setOnClickListener(v -> openFilePicker());
    }

    /**
     * إضافة الكروت من حقل الإدخال
     */
    private void addCards() {
        String cardsText = cardsInput.getText().toString().trim();
        if (cardsText.isEmpty()) {
            Toast.makeText(this, "الرجاء إدخال الكروت", Toast.LENGTH_SHORT).show();
            return;
        }

        int category = getSelectedCategory();
        if (category == -1) {
            Toast.makeText(this, "الرجاء اختيار فئة", Toast.LENGTH_SHORT).show();
            return;
        }

        // تقسيم الكروت
        String[] cardCodes = cardsText.split("\n");
        List<CardEntity> cards = new ArrayList<>();

        for (String code : cardCodes) {
            String trimmedCode = code.trim();
            if (!trimmedCode.isEmpty()) {
                cards.add(new CardEntity(trimmedCode, category));
            }
        }

        if (cards.isEmpty()) {
            Toast.makeText(this, "لم يتم العثور على كروت صحيحة", Toast.LENGTH_SHORT).show();
            return;
        }

        // إدراج الكروت في قاعدة البيانات
        new Thread(() -> {
            database.cardDao().insertCards(cards);
            runOnUiThread(() -> {
                Toast.makeText(CardsManagementActivity.this, 
                    "تمت إضافة " + cards.size() + " كرت بنجاح", 
                    Toast.LENGTH_SHORT).show();
                cardsInput.setText("");
                NotificationHelper.showSuccessNotification(this, 
                    "تمت إضافة " + cards.size() + " كرت");
            });
        }).start();
    }

    /**
     * فتح منتقي الملفات
     */
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    /**
     * الحصول على الفئة المختارة
     */
    private int getSelectedCategory() {
        // String selected = categorySpinner.getSelectedItem().toString();
        // if (selected.contains("100")) return 100;
        // if (selected.contains("200")) return 200;
        // if (selected.contains("300")) return 300;
        // if (selected.contains("500")) return 500;
        return -1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                importCardsFromFile(uri);
            }
        }
    }

    /**
     * استيراد الكروت من ملف
     */
    private void importCardsFromFile(Uri uri) {
        new Thread(() -> {
            try {
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getContentResolver().openInputStream(uri))
                );

                List<CardEntity> cards = new ArrayList<>();
                String line;
                int category = getSelectedCategory();

                if (category == -1) {
                    runOnUiThread(() -> 
                        Toast.makeText(CardsManagementActivity.this, 
                            "الرجاء اختيار فئة", 
                            Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                while ((line = reader.readLine()) != null) {
                    String trimmedLine = line.trim();
                    if (!trimmedLine.isEmpty()) {
                        cards.add(new CardEntity(trimmedLine, category));
                    }
                }

                reader.close();

                if (!cards.isEmpty()) {
                    database.cardDao().insertCards(cards);
                    runOnUiThread(() -> {
                        Toast.makeText(CardsManagementActivity.this,
                            "تمت إضافة " + cards.size() + " كرت من الملف",
                            Toast.LENGTH_SHORT).show();
                        NotificationHelper.showSuccessNotification(this,
                            "تمت إضافة " + cards.size() + " كرت من الملف");
                    });
                }

            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(CardsManagementActivity.this,
                        "خطأ في قراءة الملف: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
                    NotificationHelper.showErrorNotification(this, "خطأ في استيراد الملف");
                });
            }
        }).start();
    }
}
