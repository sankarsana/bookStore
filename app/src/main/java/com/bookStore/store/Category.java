package com.bookStore.store;

import android.database.Cursor;

import com.bookStore.App.DataBase;

public class Category {

	private long id;
	private String category;
	private float value;

	public Category() {
		id = 0;
		category = "None";
		value = 0F;
	}

	public Category(long id, String category, float value) {
		this.id = id;
		this.category = category;
		this.value = value;
	}

	public Category(long id) {
		Cursor cursor = DataBase.get().query(
				"Categories", null, "_id = " + id, null, null, null, null);
		cursor.moveToFirst();
		this.id = cursor.getLong(0);
		this.category = cursor.getString(1);
		this.value = cursor.getFloat(2);
		cursor.close();
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return category;
	}

	public float getValue() {
		return value;
	}
}
