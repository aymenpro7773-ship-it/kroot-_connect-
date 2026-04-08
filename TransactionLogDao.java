package com.kroot.connect.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kroot.connect.database.entity.TransactionLogEntity;

import java.util.List;

/**
 * واجهة الوصول إلى بيانات سجلات العمليات
 */
@Dao
public interface TransactionLogDao {

    /**
     * إدراج سجل عملية جديد
     */
    @Insert
    long insertLog(TransactionLogEntity log);

    /**
     * تحديث سجل عملية
     */
    @Update
    void updateLog(TransactionLogEntity log);

    /**
     * حذف سجل عملية
     */
    @Delete
    void deleteLog(TransactionLogEntity log);

    /**
     * الحصول على جميع السجلات
     */
    @Query("SELECT * FROM transaction_logs ORDER BY timestamp DESC")
    LiveData<List<TransactionLogEntity>> getAllLogs();

    /**
     * الحصول على السجلات حسب الحالة
     */
    @Query("SELECT * FROM transaction_logs WHERE status = :status ORDER BY timestamp DESC")
    LiveData<List<TransactionLogEntity>> getLogsByStatus(String status);

    /**
     * الحصول على السجلات حسب المبلغ
     */
    @Query("SELECT * FROM transaction_logs WHERE amount = :amount ORDER BY timestamp DESC")
    LiveData<List<TransactionLogEntity>> getLogsByAmount(int amount);

    /**
     * الحصول على السجلات حسب رقم العميل
     */
    @Query("SELECT * FROM transaction_logs WHERE customer_number = :customerNumber ORDER BY timestamp DESC")
    LiveData<List<TransactionLogEntity>> getLogsByCustomerNumber(String customerNumber);

    /**
     * الحصول على السجلات في نطاق زمني معين
     */
    @Query("SELECT * FROM transaction_logs WHERE timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp DESC")
    LiveData<List<TransactionLogEntity>> getLogsByDateRange(long startTime, long endTime);

    /**
     * عدد العمليات الناجحة اليوم
     */
    @Query("SELECT COUNT(*) FROM transaction_logs WHERE status = 'success' AND timestamp > :todayStart")
    LiveData<Integer> getSuccessfulTransactionsToday(long todayStart);

    /**
     * عدد العمليات الفاشلة اليوم
     */
    @Query("SELECT COUNT(*) FROM transaction_logs WHERE status = 'failed' AND timestamp > :todayStart")
    LiveData<Integer> getFailedTransactionsToday(long todayStart);

    /**
     * إجمالي المبيعات اليومية
     */
    @Query("SELECT SUM(amount) FROM transaction_logs WHERE status = 'success' AND timestamp > :todayStart")
    LiveData<Integer> getDailySales(long todayStart);

    /**
     * إجمالي المبيعات الشهرية
     */
    @Query("SELECT SUM(amount) FROM transaction_logs WHERE status = 'success' AND timestamp > :monthStart")
    LiveData<Integer> getMonthlySales(long monthStart);

    /**
     * إجمالي عدد العمليات
     */
    @Query("SELECT COUNT(*) FROM transaction_logs")
    LiveData<Integer> getTotalTransactions();

    /**
     * إجمالي العمليات الناجحة
     */
    @Query("SELECT COUNT(*) FROM transaction_logs WHERE status = 'success'")
    LiveData<Integer> getSuccessfulTransactions();

    /**
     * إجمالي العمليات الفاشلة
     */
    @Query("SELECT COUNT(*) FROM transaction_logs WHERE status = 'failed'")
    LiveData<Integer> getFailedTransactions();

    /**
     * حذف جميع السجلات
     */
    @Query("DELETE FROM transaction_logs")
    void deleteAllLogs();

    /**
     * حذف السجلات القديمة (أقدم من 30 يوم)
     */
    @Query("DELETE FROM transaction_logs WHERE timestamp < :thirtyDaysAgo")
    void deleteOldLogs(long thirtyDaysAgo);

    /**
     * الحصول على آخر سجل
     */
    @Query("SELECT * FROM transaction_logs ORDER BY timestamp DESC LIMIT 1")
    TransactionLogEntity getLastLog();
}
