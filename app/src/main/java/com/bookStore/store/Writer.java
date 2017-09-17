package com.bookStore.store;

import android.database.Cursor;

import com.bookStore.App.DataBase;

public class Writer {

	private long id;

	private String name;

	public Writer() {
		id = 1;
		name = "Шрила Прабхупада";
	}

	public Writer(long id, String writerName) {
		this.id = id;
		this.name = writerName;
	}

	public static String getWriterName(long id) {
		String writerName = null;
		Cursor cursor = DataBase.get().rawQuery(
				"SELECT writer FROM writers WHERE _id = " + id, null);
		if (cursor.moveToFirst())
			writerName = cursor.getString(0);
		cursor.close();
		return writerName;
	}

	public String getName() {
		return name;
	}

	public void setName(String writerName) {
		this.name = writerName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
