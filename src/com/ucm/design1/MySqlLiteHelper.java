package com.ucm.design1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySqlLiteHelper extends SQLiteOpenHelper {

	public static final String tableName = "blacklist";
	public static final String columnId = "id";
	public static final String columnNumber = "number";
	public static final String columnName = "name";
	public static final String databaseName = "ucm.db";
	public static final int version = 1;
	public static final String databaseCreate = "create table " + tableName
			+ "(" + columnId + " text primary key, " + columnName + " text,"
			+ columnNumber + " text not null);";

	public MySqlLiteHelper(Context context) {
		super(context, databaseName, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(databaseCreate);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySqlLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS" + tableName);
		onCreate(db);

	}

}
