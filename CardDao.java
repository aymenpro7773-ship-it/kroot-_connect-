package com.kroot.connect.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kroot.connect.database.entity.CardEntity;

import java.util.List;

/**
 * واجهة الوصول إلى بيانات الكروت (Data Access Object)
 */
@Dao
public interface CardDao {

    /**
     * إدراج كرت جديد
     */
    @Insert
    long insertCard(CardEntity card);

    /**
     * إدراج عدة كروت
     */
    @Insert
    void insertCards(List<CardEntity> cards);

    /**
     * تحديث كرت
     */
    @Update
    void updateCard(CardEntity card);

    /**
     * حذف كرت
     */
    @Delete
    void deleteCard(CardEntity card);

    /**
     * الحصول على جميع الكروت
     */
    @Query("SELECT * FROM cards ORDER BY created_at DESC")
    LiveData<List<CardEntity>> getAllCards();

    /**
     * الحصول على الكروت المتاحة من فئة معينة
     */
    @Query("SELECT * FROM cards WHERE category = :category AND status = 'available' LIMIT 1")
    CardEntity getAvailableCardByCategory(int category);

    /**
     * الحصول على جميع الكروت المتاحة من فئة معينة
     */
    @Query("SELECT * FROM cards WHERE category = :category AND status = 'available'")
    LiveData<List<CardEntity>> getAvailableCardsByCategory(int category);

    /**
     * الحصول على عدد الكروت المتاحة من فئة معينة
     */
    @Query("SELECT COUNT(*) FROM cards WHERE category = :category AND status = 'available'")
    LiveData<Integer> getAvailableCardsCount(int category);

    /**
     * الحصول على عدد الكروت المستخدمة من فئة معينة
     */
    @Query("SELECT COUNT(*) FROM cards WHERE category = :category AND status = 'used'")
    LiveData<Integer> getUsedCardsCount(int category);

    /**
     * الحصول على إجمالي عدد الكروت من فئة معينة
     */
    @Query("SELECT COUNT(*) FROM cards WHERE category = :category")
    LiveData<Integer> getTotalCardsCount(int category);

    /**
     * الحصول على الكروت حسب الحالة
     */
    @Query("SELECT * FROM cards WHERE status = :status ORDER BY created_at DESC")
    LiveData<List<CardEntity>> getCardsByStatus(String status);

    /**
     * حذف كل الكروت المستخدمة
     */
    @Query("DELETE FROM cards WHERE status = 'used'")
    void deleteUsedCards();

    /**
     * الحصول على كرت معين برقم المعرف
     */
    @Query("SELECT * FROM cards WHERE id = :cardId")
    CardEntity getCardById(int cardId);

    /**
     * البحث عن كرت بالكود
     */
    @Query("SELECT * FROM cards WHERE card_code = :cardCode")
    CardEntity getCardByCode(String cardCode);

    /**
     * حذف جميع الكروت
     */
    @Query("DELETE FROM cards")
    void deleteAllCards();
}
