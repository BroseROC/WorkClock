package com.broseadventures.workclock;

import android.database.*;

public class Helpers {

	public static String DBGetString(Cursor c, String columnName) {
		int colId = c.getColumnIndex(columnName);
		return c.getString(colId);
	}
	
	public static double DBGetDouble(Cursor c, String columnName) {
		int colId = c.getColumnIndex(columnName);
		return c.getDouble(colId);
	}

	public static long DBGetLong(Cursor c, String columnName) {
		int colId = c.getColumnIndex(columnName);
		return c.getLong(colId);
	}

}
