package cn.bigorange.wheel.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.bigorange.wheel.MyApplication;
import cn.bigorange.wheel.entity.Record;

import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "test.db";

    private DatabaseHelper() {
        super(MyApplication.getInstance().getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 单例持有者
    private static class SingletonHolder {
        private static final DatabaseHelper INSTANCE = new DatabaseHelper();

        private SingletonHolder() {
        }
    }

    // 调用内部类属性
    public static DatabaseHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBNumbersRecords.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public synchronized void insertRecord(Record record) {
        DBNumbersRecords.insertRecord(getWritableDatabase(), record);
    }

    public synchronized int deleteRecordById(long id) {
        return DBNumbersRecords.deleteRecordById(getWritableDatabase(), id);
    }

    public synchronized void deleteAllRecords() {
        DBNumbersRecords.deleteAllRecords(getWritableDatabase());
    }

    public synchronized int deleteSelectedRecords(List<Long> idList) {
        return DBNumbersRecords.deleteSelectedRecords(getWritableDatabase(), idList);
    }

    public synchronized List<Record> getAllRecords() {
        return DBNumbersRecords.getAllRecords(getWritableDatabase());
    }

    public synchronized List<Record> getAllRecordsByStartIndex(int startIndex, int pageSize) {
        return DBNumbersRecords.getAllRecordsByStartIndex(getWritableDatabase(), startIndex, pageSize);
    }

    public synchronized Record selectRecordById(long id) {
        return DBNumbersRecords.selectRecordById(getWritableDatabase(), id);
    }


}
