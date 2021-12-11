package cn.bigorange.wheel.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cn.bigorange.wheel.entity.Record;


public class DBNumbersRecords {

    public static final String TABLE_NAME = "record";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY autoincrement,"
                    + COLUMN_TITLE + " TEXT,"
                    + COLUMN_CONTENT + " TEXT"
                    + " )";

    //清空数据//自增长ID为0

    public static final String SELECT_ALL = " SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC";

    public static final String SELECT_ALL_PAGE = " SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC limit ? offset ?  ";

    public static final String SELECT_BY_ID = " SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " =?  ";

    static void insertRecord(SQLiteDatabase db, Record item) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, item.getTitle());
        if (item.getOptionList() == null) {
            item.setOptionList(new ArrayList<>());
        }
        Gson gson = new Gson();
        String json = gson.toJson(item.getOptionList());
        values.put(COLUMN_CONTENT, json);
        db.insert(TABLE_NAME, null, values);
    }

    static int deleteRecordById(SQLiteDatabase db, long id) {
        return db.delete(TABLE_NAME, COLUMN_ID + " =? ", new String[]{String.valueOf(id)});
    }

    static Record selectRecordById(SQLiteDatabase db, long id) {
        Record item = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(SELECT_BY_ID, new String[]{String.valueOf(id)});
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    item = new Record();
                    item.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                    item.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                    String content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));
                    if (StringUtils.isTrimEmpty(content)) {
                        item.setOptionList(new ArrayList<>());
                    } else {
                        Gson gson = new Gson();
                        List<String> optionList = gson.fromJson(content, new TypeToken<List<String>>() {
                        }.getType());
                        item.setOptionList(optionList);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return item;
    }

    static void deleteAllRecords(SQLiteDatabase db) {
        try {
            db.execSQL("DELETE FROM " + TABLE_NAME);//Delete all records of table
            db.execSQL("UPDATE  sqlite_sequence SET seq = 0 where name = '" + TABLE_NAME + "'");//清空数据和自增长ID Reset the auto_increment primary key if you needed
            db.execSQL("VACUUM ");//释放空间 For go back free space by shrinking sqlite file
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static int deleteSelectedRecords(SQLiteDatabase db, List<Long> idList) {
        int result = 0;
        if (idList == null || idList.size() <= 0) {
            return result;
        }
        List<String> list = new ArrayList<>();
        for (Long aLong : idList) {
            if (aLong != null) {
                list.add(Long.toString(aLong));
            }
        }
        if (list.size() <= 0) {
            return result;
        }
        String idListStr = org.apache.commons.lang3.StringUtils.join(list, ",");
        try {
            return db.delete(TABLE_NAME, " id in (" + idListStr + ") ", null);//Delete all records of table
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    static List<Record> getAllRecords(SQLiteDatabase db) {
        List<Record> result = new ArrayList<>();
        Record item = null;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(SELECT_ALL, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    item = new Record();
                    item.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                    item.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                    String content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));
                    if (StringUtils.isTrimEmpty(content)) {
                        item.setOptionList(new ArrayList<>());
                    } else {
                        Gson gson = new Gson();
                        List<String> optionList = gson.fromJson(content, new TypeToken<List<String>>() {
                        }.getType());
                        item.setOptionList(optionList);
                    }
                    result.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return result;
    }

    /**
     * 分批加载数据
     *
     * @param db
     * @param startIndex 从哪个位置开始加载数据
     * @param pageSize   最多加载几条数据
     * @return
     */
    static List<Record> getAllRecordsByStartIndex(SQLiteDatabase db, int startIndex, int pageSize) {
        List<Record> result = new ArrayList<>();
        Record item = null;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(SELECT_ALL_PAGE, new String[]{String.valueOf(pageSize), String.valueOf(startIndex)});
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    item = new Record();
                    item.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                    item.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                    String content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));
                    if (StringUtils.isTrimEmpty(content)) {
                        item.setOptionList(new ArrayList<>());
                    } else {
                        Gson gson = new Gson();
                        List<String> optionList = gson.fromJson(content, new TypeToken<List<String>>() {
                        }.getType());
                        item.setOptionList(optionList);
                    }
                    result.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return result;
    }
}