package cn.bigorange.wheel.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import cn.bigorange.wheel.MyApplication;
import cn.bigorange.wheel.entity.Record;
import cn.bigorange.wheel.entity.Roster;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

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
        db.execSQL(DBNumbersRosters.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL(DBNumbersRosters.CREATE_TABLE);
        }
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

    public synchronized List<Record> getAllRecords(boolean hasOptionList) {
        return DBNumbersRecords.getAllRecords(getWritableDatabase(), hasOptionList);
    }

    public synchronized List<Record> getAllRecordsByStartIndex(int startIndex, int pageSize) {
        return DBNumbersRecords.getAllRecordsByStartIndex(getWritableDatabase(), startIndex, pageSize);
    }

    public synchronized int updateRecordById(Record record) {
        return DBNumbersRecords.updateRecordById(getWritableDatabase(), record);
    }

    public synchronized Record selectRecordById(long id) {
        return DBNumbersRecords.selectRecordById(getWritableDatabase(), id);
    }

    //----------------------------------------------------------------------------------------------

    public synchronized void insertRoster(Roster roster) {
        DBNumbersRosters.insertRoster(getWritableDatabase(), roster);
    }

    public synchronized int deleteRosterById(long id) {
        return DBNumbersRosters.deleteRosterById(getWritableDatabase(), id);
    }

    public synchronized void deleteAllRosters() {
        DBNumbersRosters.deleteAllRosters(getWritableDatabase());
    }

    public synchronized int deleteSelectedRosters(List<Long> idList) {
        return DBNumbersRosters.deleteSelectedRosters(getWritableDatabase(), idList);
    }

    public synchronized List<Roster> getAllRosters(boolean hasOptionList) {
        return DBNumbersRosters.getAllRosters(getWritableDatabase(), hasOptionList);
    }

    public synchronized List<Roster> getAllRostersByStartIndex(int startIndex, int pageSize) {
        return DBNumbersRosters.getAllRostersByStartIndex(getWritableDatabase(), startIndex, pageSize);
    }

    public synchronized int updateRosterById(Roster roster) {
        return DBNumbersRosters.updateRosterById(getWritableDatabase(), roster);
    }

    public synchronized Roster selectRosterById(long id) {
        return DBNumbersRosters.selectRosterById(getWritableDatabase(), id);
    }
}
