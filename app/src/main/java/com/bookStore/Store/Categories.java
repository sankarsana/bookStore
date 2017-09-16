package com.bookStore.Store;

import android.database.Cursor;

import com.bookStore.App.DataBase;

import java.util.ArrayList;

public class Categories extends ArrayList<Category> {

	private static Categories instance;

	private Categories() {
		super();
		Cursor cursor = DataBase.get().query("Categories", null, null, null, null, null, null);
		cursor.moveToFirst();
		do {
			add(new Category(
					cursor.getLong(0),
					cursor.getString(cursor.getColumnIndex("category")),
					cursor.getFloat(cursor.getColumnIndex("value"))));
		} while (cursor.moveToNext());
		cursor.close();
	}

	public static Categories getInstance() {
		if (instance == null)
			instance = new Categories();
		return instance;
	}

	public Category getCategoryOrNone(String value) {
		for (Category cat : this) {
			if (cat.getName().equalsIgnoreCase(value))
				return cat;
		}
		return get(0);
	}

	public Category getCategory(int categoryId) {
		return get(categoryId);
	}
}
