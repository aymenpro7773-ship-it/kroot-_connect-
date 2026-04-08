package com.kroot.connect.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * كيان الكرت - يمثل كروت الرد في قاعدة البيانات
 */
@Entity(tableName = "cards")
public class CardEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "card_code")
    private String cardCode;

    @ColumnInfo(name = "category")
    private int category; // 100, 200, 300, 500, 1000

    @ColumnInfo(name = "status")
    private String status; // "available" أو "used"

    @ColumnInfo(name = "created_at")
    private long createdAt;

    @ColumnInfo(name = "used_at")
    private long usedAt;

    @ColumnInfo(name = "used_by_number")
    private String usedByNumber; // رقم العميل الذي استخدم الكرت

    // البناء
    public CardEntity() {
    }

    public CardEntity(String cardCode, int category) {
        this.cardCode = cardCode;
        this.category = category;
        this.status = "available";
        this.createdAt = System.currentTimeMillis();
        this.usedAt = 0;
    }

    // Getters و Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(long usedAt) {
        this.usedAt = usedAt;
    }

    public String getUsedByNumber() {
        return usedByNumber;
    }

    public void setUsedByNumber(String usedByNumber) {
        this.usedByNumber = usedByNumber;
    }
}
