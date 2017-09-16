package com.bookStore.Cards;

import android.database.Cursor;

public class CardEntry {

	long bookId;
	String bookName;
	int count;
	int get;
	int distr;
	int ret;
	String shortName;

	public CardEntry(Cursor c) {
		bookId = c.getLong(0);
		bookName = c.getString(1);
		shortName = c.getString(2);
		count = c.getInt(3);
		get = c.getInt(4);
		distr = c.getInt(5);
		ret = c.getInt(6);
	}

	public String getCountString() {
		return Integer.toString(count);
	}

	public String getGetString() {
		return Integer.toString(get);
	}

	public String getDistrString() {
		return Integer.toString(distr);
	}

	public String getRetString() {
		return Integer.toString(ret);
	}
}
