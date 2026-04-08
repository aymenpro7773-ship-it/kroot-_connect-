package com.kroot.connect.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * كيان سجل العمليات - يسجل جميع عمليات الرد التلقائي
 */
@Entity(tableName = "transaction_logs")
public class TransactionLogEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "amount")
    private int amount; // المبلغ المحول

    @ColumnInfo(name = "customer_number")
    private String customerNumber; // رقم العميل

    @ColumnInfo(name = "card_code")
    private String cardCode; // كود الكرت المرسل

    @ColumnInfo(name = "status")
    private String status; // "success", "failed", "pending"

    @ColumnInfo(name = "original_message")
    private String originalMessage; // الرسالة الأصلية

    @ColumnInfo(name = "error_message")
    private String errorMessage; // رسالة الخطأ إن وجدت

    // البناء
    public TransactionLogEntity() {
    }

    public TransactionLogEntity(long timestamp, int amount, String customerNumber, 
                               String cardCode, String status) {
        this.timestamp = timestamp;
        this.amount = amount;
        this.customerNumber = customerNumber;
        this.cardCode = cardCode;
        this.status = status;
    }

    // Getters و Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOriginalMessage() {
        return originalMessage;
    }

    public void setOriginalMessage(String originalMessage) {
        this.originalMessage = originalMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
