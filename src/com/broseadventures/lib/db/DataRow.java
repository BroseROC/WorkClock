package com.broseadventures.lib.db;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import android.database.Cursor;

public class DataRow {

    public DataRow() {
        this.init();
    }

    public DataRow(String[] objects, String[] columnNames) throws IllegalArgumentException {
        if (objects.length != columnNames.length) {
            throw new IllegalArgumentException("Length of objects array must match length of column names array");
        }
        this.init(objects, columnNames);
    }

    public DataRow(Cursor c) {
        
        if (c.isAfterLast() || c.isBeforeFirst() || c.isClosed()) {
            init();
            return;
        }
        ArrayList<String> data = new ArrayList<String>(c.getColumnCount());
        for (int i = 0; i < c.getColumnCount(); i++) {
            data.add(c.getString(i));
        }
        this.init((String[]) data.toArray(new String[data.size()]), c.getColumnNames());
    }

    private void init() {
        this.init(new String[0], new String[0]);
    }

    private void init(String[] objects, String[] columnNames) {
        this.objects = objects;
        this.columnNames = new ArrayList<String>(Arrays.asList(columnNames));
    }

    private String[] objects;
    private ArrayList<String> columnNames;

    public int ColumnCount() {
        return this.objects.length;
    }

    public ArrayList<String> GetColumnNames() {
        return this.columnNames;
    }

    /**
     * 
     * @param columnName
     * @return Index of the column with that name, or -1
     */
    public int getColumnIndex(String columnName) {
        return this.columnNames.indexOf(columnName);
    }

    /**
     * 
     * @param columnIndex
     * @return The string, or null
     */
    public String GetString(int columnIndex) {
        try {
            return this.objects[columnIndex];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * 
     * @param columnName
     * @return
     */
    public String GetString(String columnName) throws NumberFormatException {
        return this.GetString(this.getColumnIndex(columnName));
    }

    /**
     * @param columnIndex
     * @throws NumberFormatException
     */
    public int GetInt(int columnIndex) throws NumberFormatException {
        return Integer.parseInt(this.objects[columnIndex]);
    }

    /**
     * @param columnName
     * @throws NumberFormatException
     */
    public int GetInt(String columnName) throws NumberFormatException {
        return this.GetInt(this.getColumnIndex(columnName));
    }

    /**
     * @param columnIndex
     * @throws NumberFormatException
     */
    public double GetDouble(int columnIndex) throws NumberFormatException {
        return Double.parseDouble(this.objects[columnIndex]);
    }

    /**
     * @param columnName
     * @throws NumberFormatException
     */
    public double GetDouble(String columnName) throws NumberFormatException {
        return this.GetDouble(this.getColumnIndex(columnName));
    }

    /**
     * @param columnIndex
     * @throws NumberFormatException
     */
    public long GetLong(int columnIndex) throws NumberFormatException {
        return Long.parseLong(this.objects[columnIndex]);
    }

    /**
     * @param columnName
     * @throws NumberFormatException
     */
    public long GetLong(String columnName) throws NumberFormatException {
        return this.GetLong(this.getColumnIndex(columnName));
    }

    /**
     * @param columnIndex
     * @throws ParseException
     */
    public Date GetDate(int columnIndex) throws ParseException {
        String s = this.objects[columnIndex];
        try {
            long ldate = Long.parseLong(s);
            return new Date(ldate);
        } catch (NumberFormatException e) {
            return DateFormat.getDateInstance().parse(s);
        }
    }

    /**
     * @param columnName
     * @throws ParseException
     */
    public Date GetDate(String columnName) throws ParseException {
        return this.GetDate(this.getColumnIndex(columnName));
    }

}
