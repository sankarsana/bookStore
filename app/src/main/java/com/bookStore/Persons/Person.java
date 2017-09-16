package com.bookStore.Persons;

import android.database.Cursor;

import com.bookStore.App.DataBase;

public class Person {

	public static final long STORE_ID = 1;
	public long id;
	public String name = "";
	public int percent;

	public Person() {
		id = -1;
		percent = 100;
	}

	public Person(long id, String name, int percent) {
		this.id = id;
		this.name = name;
		this.percent = percent;
	}

	private Person(long personId) {
		Cursor cursor = DataBase.get().query(
				"Persons", null, "_id" + "=" + personId, null, null, null, null);
		cursor.moveToFirst();
		id = personId;
		name = cursor.getString(1);
		percent = cursor.getInt(2);
		cursor.close();
	}

	public static Person getPerson(long id) {
		return new Person(id);
	}

	public int getInStockCount(long bookId) {
		Cursor c = DataBase.get().query("InStock", null, "personId = ? AND bookId = ?",
				new String[]{Long.toString(id), Long.toString(bookId)}, null, null, null);
		int ret = c.moveToFirst() ? c.getInt(2) : 0;
		c.close();
		return ret;
	}
}
