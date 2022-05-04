package cn.bigorange.wheel.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.bigorange.common.utils.ListUtils;
import cn.bigorange.wheel.entity.Roster;

public class DBNumbersRosters {

    public static final String TABLE_NAME = "roster";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_OPTIONS = "options";

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY autoincrement,"
                    + COLUMN_TITLE + " TEXT,"
                    + COLUMN_OPTIONS + " TEXT"
                    + " )";

    //清空数据//自增长ID为0

    public static final String SELECT_ALL = " SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC";

    public static final String SELECT_ALL_PAGE = " SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC limit ? offset ?  ";

    public static final String SELECT_BY_ID = " SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " =?  ";

    static void insertRoster(SQLiteDatabase db, Roster item) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, item.getTitle());
        String json = ListUtils.listToString(item.getOptionList());
        values.put(COLUMN_OPTIONS, json);
        db.insert(TABLE_NAME, null, values);
    }

    static int deleteRosterById(SQLiteDatabase db, long id) {
        return db.delete(TABLE_NAME, COLUMN_ID + " =? ", new String[]{String.valueOf(id)});
    }

    static int updateRosterById(SQLiteDatabase db, Roster item) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, item.getTitle());
        String json = ListUtils.listToString(item.getOptionList());
        values.put(COLUMN_OPTIONS, json);
        return db.update(TABLE_NAME, values, COLUMN_ID + " =? ", new String[]{String.valueOf(item.getId())});
    }

    static Roster selectRosterById(SQLiteDatabase db, long id) {
        Roster item = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(SELECT_BY_ID, new String[]{String.valueOf(id)});
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    item = new Roster();
                    item.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                    item.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                    String options = cursor.getString(cursor.getColumnIndex(COLUMN_OPTIONS));
                    item.setOptionList(ListUtils.stringToList(options));
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

    static void deleteAllRosters(SQLiteDatabase db) {
        try {
            db.execSQL("DELETE FROM " + TABLE_NAME);//Delete all rosters of table
            db.execSQL("UPDATE  sqlite_sequence SET seq = 0 where name = '" + TABLE_NAME + "'");//清空数据和自增长ID Reset the auto_increment primary key if you needed
            db.execSQL("VACUUM ");//释放空间 For go back free space by shrinking sqlite file
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static int deleteSelectedRosters(SQLiteDatabase db, List<Long> idList) {
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
        String idListStr = org.apache.commons.lang3.StringUtils.join(list, ", ");
        try {
            return db.delete(TABLE_NAME, " id in (" + idListStr + ") ", null);//Delete all rosters of table
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    static List<Roster> getAllRosters(SQLiteDatabase db, boolean hasOptionList) {
        List<Roster> result = new ArrayList<>();
        Roster item = null;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(SELECT_ALL, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    item = new Roster();
                    item.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                    item.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                    if (hasOptionList) {
                        String options = cursor.getString(cursor.getColumnIndex(COLUMN_OPTIONS));
                        item.setOptionList(ListUtils.stringToList(options));
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
    static List<Roster> getAllRostersByStartIndex(SQLiteDatabase db, int startIndex, int pageSize) {
        List<Roster> result = new ArrayList<>();
        Roster item = null;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(SELECT_ALL_PAGE, new String[]{String.valueOf(pageSize), String.valueOf(startIndex)});
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    item = new Roster();
                    item.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                    item.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                    String options = cursor.getString(cursor.getColumnIndex(COLUMN_OPTIONS));
                    item.setOptionList(ListUtils.stringToList(options));
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
