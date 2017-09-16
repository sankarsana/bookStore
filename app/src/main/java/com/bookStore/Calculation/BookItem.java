package com.bookStore.Calculation;

import android.database.Cursor;

public class BookItem {

	long bookId;
	String bookName;
	String shortName;
	String progressString;
	String previousProgressString;
	int count;
	int previousCount;

	public BookItem(Cursor cursor) {
		bookId = cursor.getLong(cursor.getColumnIndex("bookId"));
		bookName = cursor.getString(cursor.getColumnIndex("bookName"));
		shortName = cursor.getString(cursor.getColumnIndex("shortName"));
		progressString = cursor.getString(cursor.getColumnIndex("progress"));
		previousProgressString = cursor.getString(cursor.getColumnIndex("previousProgress"));
		count = cursor.getInt(cursor.getColumnIndex("count"));
		previousCount = cursor.getInt(cursor.getColumnIndex("previousCount"));
	}
}
