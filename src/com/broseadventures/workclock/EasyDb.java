package com.broseadventures.workclock;

import com.broseadventures.lib.data.*;
import com.broseadventures.lib.db.*;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EasyDb extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    // private String databaseName;
    private final static String TAG = "WorkClock::EasyDb";
    private final static int DATABASE_VERSION = 1;
    private final static String JOB_CREATE = "CREATE TABLE IF NOT EXISTS Job (PayRate NUMERIC NOT NULL, Name TEXT NOT NULL PRIMARY KEY)";
    private final static String SESSION_CREATE = "CREATE TABLE IF NOT EXISTS Session (SessionId INTEGER PRIMARY KEY AUTOINCREMENT, JobName TEXT NOT NULL, PunchIn TEXT NOT NULL, PunchOut TEXT NOT NULL, Elapsed TEXT NOT NULL)";

    public EasyDb(Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
        this.init();
    }

    public void init() {
        Log.d(TAG, "Entering Init");
        db = this.getWritableDatabase();
        //this.onUpgrade(db, 0, 1);
        //this.onCreate(db);
        Log.d(TAG, "Leaving GetAllSessions");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Entering onCreate");
        db.execSQL(JOB_CREATE);
        db.execSQL(SESSION_CREATE);
        Log.d(TAG, "Leaving onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Entering onUpgrade");
        Log.d(TAG, "Upgrading database from " + oldVersion + " to " + newVersion);
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS Job");
            db.execSQL("DROP TABLE IF EXISTS Session");
        }
        Log.d(TAG, "Leaving onUpgrade");
    }

    public void Delete(String tableName, ValuesMap where) {
        Log.d(TAG, "Entering Delete");
        Log.d(TAG, "Deleting from '" + tableName + "' " + where.toString());
        String sql = "DELETE FROM " + tableName + " WHERE 1=1 ";
        for (StrValue val : where.GetAll()) {
            sql += " AND " + val.key + "= '" + val.value + "'";
        }
        this.db.execSQL(sql);
        Log.d(TAG, "Leaving Delete");
    }

    /**
     * Runs an insert, create, update, etc
     * 
     * @param table
     * @param data
     * @throws SQLException
     */
    public void Command(String table, StrValue[] data) throws SQLException {
        Log.d(TAG, "Entering Command(String, StrValue[]");
        StringBuilder sqlB = new StringBuilder("INSERT INTO " + table + "(");
        StringBuilder valuesB = new StringBuilder(" VALUES (");
        for (StrValue sqlValue : data) {
            sqlB.append("'" + sqlValue.key + "', ");
            valuesB.append("'" + sqlValue.value + "', ");
        }
        String sql = sqlB.substring(0, sqlB.lastIndexOf(","));
        String values = valuesB.substring(0, valuesB.lastIndexOf(","));
        sql += ")";
        values += ")";
        this.db.execSQL(sql + " " + values);
        Log.d(TAG, "Leaving Command(String, StrValue[]");
    }

    /**
     * Runs an insert, create, update, etc. Returns nothing
     * 
     * @param sql
     * @throws SQLException
     */
    public void Command(String sql) throws SQLException {
        Log.d(TAG, "Entering Command(String");
        this.db.execSQL(sql);
        Log.d(TAG, "Leaving Command(String");
    }

    public DataTable GetDataTable(String sql) throws SQLException {
        Log.d(TAG, "Entering GetDataTable(String");
        Cursor c = this.db.rawQuery(sql, null);
        Log.d(TAG, "Leaving GetDataTable(String");
        return new DataTable(c);
    }

    /**
     * 
     * @param sql
     * @return The first row
     */
    public DataRow GetDataRow(String sql) {
        Log.d(TAG, "Entering GetDataRow(String");
        Cursor c = this.db.rawQuery(sql, null);
        c.moveToFirst();
        Log.d(TAG, "Leaving GetDataRow(String");
        return new DataRow(c);
    }

    /**
     * @param sql
     *            A SQL query
     * @return The value from the first column of the first row as an int
     * @throws ClassCastException
     */
    public int GetInt(String sql, int defaultValue) {
        try {
            return this.GetDataRow(sql).GetInt(0);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
