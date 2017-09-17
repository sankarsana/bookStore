package com.bookStore.Store;

import com.bookStore.App.DataBase;

public class Store {

	public static void updateBookCount(long bookId, int value) {
		DataBase.get().execSQL("UPDATE books SET count = (count + '" + value + "') WHERE _id = " + bookId);
	}

	public static void setBookCount(long bookId, int count) {
		DataBase.get().execSQL("UPDATE books SET count = '" + count + "' WHERE _id = " + bookId);
	}

}
