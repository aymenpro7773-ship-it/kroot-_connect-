package com.kroot.connect.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kroot.connect.database.dao.CardDao;
import com.kroot.connect.database.dao.TransactionLogDao;
import com.kroot.connect.database.entity.CardEntity;
import com.kroot.connect.database.entity.TransactionLogEntity;

/**
 * قاعدة البيانات الرئيسية للتطبيق
 * تدير جميع البيانات المحلية المشفرة
 */
@Database(
    entities = {CardEntity.class, TransactionLogEntity.class},
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract CardDao cardDao();
    public abstract TransactionLogDao transactionLogDao();

    /**
     * الحصول على مثيل قاعدة البيانات (Singleton)
     */
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "kroot_connect_db"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
