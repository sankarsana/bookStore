package com.bookStore.Import;

import android.content.ContentValues;
import android.database.Cursor;

import com.bookStore.App.DataBase;
import com.bookStore.App.Utilities;

public class ImportDates {

	private Cursor cursor;

	public ImportDates() {
		refresh();
		cursor.moveToFirst();
	}

	public long getId() {
		return cursor.getLong(0);
	}

	public String getDateStringSimple() {
		return Utilities.convertDateDBToSimple(cursor.getString(1));
	}

	public String getDateStringExtend() {
		return Utilities.convertDateDBToExtend(cursor.getString(1));
	}

	public int getCount() {
		return cursor.getCount();
	}

	public void refresh() {
		cursor = DataBase.get().query("ImportDate", null, null, null, null, null,
				"dateFld");
	}

	public void setCurrentDate_insertIfNotExists() {
		long currentDate = Utilities.getCurrentDate_DB_long();
		Cursor c = DataBase.get().query("ImportDate", null, "dateFld = " + currentDate,
				null, null, null, null);
		if (!c.moveToFirst()) {
			ContentValues values = new ContentValues();
			values.put("dateFld", currentDate);
			DataBase.get().insert("ImportDate", "dateFld", values);
			refresh();
		}
		c.close();
		moveToLast();
	}

	public int deleteDate() {
		return DataBase.get().delete("ImportDate", "_id = " + getDateId(), null);
	}

	public long getDateId() {
		return cursor.getLong(0);
	}

	public boolean moveToNext() {
		return cursor.moveToNext();
	}

	public boolean moveToLast() {
		return cursor.moveToLast();
	}

	public boolean moveToIdPosition(long dateId) {
		cursor.moveToFirst();
		do {
			if (cursor.getLong(0) == dateId)
				return true;
		} while (cursor.moveToNext());
		return false;
	}

	@Override
	public void finalize() {
		cursor.close();
		try {
			super.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
