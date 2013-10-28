package com.broseadventures.lib.db;

import java.util.ArrayList;
import java.util.Arrays;

import android.database.Cursor;

public class DataTable {

    public DataTable() {
        this.rows = new DataRow[0];
        this.columnNames = new ArrayList<String>();
    }

    public DataTable(DataRow[] rows) {
        this.rows = rows;
        if (rows.length > 0) {
            this.columnNames = rows[0].GetColumnNames();
        } else {
            this.columnNames = new ArrayList<String>();
        }
    }

    public DataTable(Cursor c) {
        this.rows = new DataRow[c.getCount()];
        this.columnNames = new ArrayList<String>(Arrays.asList(c.getColumnNames()));
        if (c.moveToFirst() == false) {
            return;
        }
        for (int i = 0; !c.isAfterLast(); i++) {
            this.rows[i] = new DataRow(c);
            if(c.moveToNext() == false){
                break;
            }
        }
    }

    private ArrayList<String> columnNames;
    private DataRow[] rows;

    public int RowCount() {
        return this.rows.length;
    }

    public DataRow[] GetRows() {
        return this.rows;
    }

    public ArrayList<String> GetColumnNames() {
        return columnNames;
    }

    public void SetColumnNames(ArrayList<String> columnNames) {
        this.columnNames = columnNames;
    }

}
