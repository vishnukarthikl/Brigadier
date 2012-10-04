package com.ucm.design1;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BlackListDataSource {
	private SQLiteDatabase database;
	private MySqlLiteHelper dbHelper;
	private String[] allColumns = { MySqlLiteHelper.columnId,
			MySqlLiteHelper.columnName, MySqlLiteHelper.columnNumber };

	
	public BlackListDataSource(Context context) {
		dbHelper = new MySqlLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
		
		
	}

	public void close() throws SQLException {
		dbHelper.close();
	}

	public void createBlackContact(Contact contact) {
		ContentValues values = new ContentValues();

		if (!contact.isSynced) {
			// getting id from the server database
		} else {
			values.put(MySqlLiteHelper.columnId, contact.id);
			values.put(MySqlLiteHelper.columnName, contact.name);
			values.put(MySqlLiteHelper.columnNumber, contact.number);
			database.insert(MySqlLiteHelper.tableName, null, values);
		}

	}
	
	public void flushTable(String tableName)
	{
	
		database.execSQL("Delete from " + tableName);
	}
	
	public List<Contact> getAllBlackContacts()
	{
		List<Contact> contacts=new ArrayList<Contact>();
		Cursor cursor=database.query(MySqlLiteHelper.tableName,allColumns,null,null,null,null,null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Contact contact=new Contact(cursor.getString(0),cursor.getString(1),cursor.getString(2));
			contacts.add(contact);
			cursor.moveToNext();
			
		}
		cursor.close();
		return contacts;
	}

	
}
