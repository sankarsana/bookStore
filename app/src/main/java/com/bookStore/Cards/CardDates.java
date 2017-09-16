package com.bookStore.Cards;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bookStore.App.DataBase;
import com.bookStore.App.Utilities;
import com.bookStore.Persons.Person;
import com.bookStore.Persons.PersonsActivity;

public class CardDates extends FragmentStatePagerAdapter {

	private Cursor datesCursor;
	private Person person;

	public CardDates(FragmentManager fm) {
		super(fm);
		person = PersonsActivity.getPerson();
		refreshCursor();
		setDateToday();
	}

	private void refreshCursor() {
		datesCursor = DataBase.get().rawQuery(
				"SELECT * FROM CardDate WHERE personId = " + person.id + " ORDER BY dateFld", null);
		datesCursor.moveToFirst();
	}

	public void setDateToday() {
		if (datesCursor.moveToLast())
			if (datesCursor.getLong(1) == Utilities.getCurrentDate_DB_long())
				return;
		insertDate(Utilities.getCurrentDate_DB_long());
		refreshCursor();
	}

	private long insertDate(long date) {
		ContentValues values = new ContentValues();
		values.put("dateFld", date);
		values.put("personId", person.id);
		values.put("percent", person.percent);
		return DataBase.get().insert("CardDate", "dateFld", values);
	}

	/**
	 * @param newDate new Date
	 * @return position in cursor
	 */
	public int insertNewDate(long newDate) {
		datesCursor.moveToFirst();
		do {
			if (datesCursor.getLong(datesCursor.getColumnIndex("dateFld")) == newDate)
				return datesCursor.getPosition();
		} while (datesCursor.moveToNext());
		long dateId = insertDate(newDate);
		refreshCursor();
		do {
			if (datesCursor.getLong(0) == dateId)
				return datesCursor.getPosition();
		} while (datesCursor.moveToNext());
		return -1;
	}

	public void deleteDateTodayIfItEmpty() {
		Cursor c = DataBase.get().rawQuery("SELECT CardDate._id FROM CardDate " +
				"LEFT OUTER JOIN CardEntries ON CardDate._id = CardEntries.dateId " +
				"WHERE CardEntries._id IS NULL", null);
		while (c.moveToNext()) {
			DataBase.get().delete("CardDate", "_id = " + c.getString(0), null);
		}
		c.close();
	}

	@Override
	public Fragment getItem(int position) {
		datesCursor.moveToPosition(position);
		return new CardDateFragment(new CardDate(datesCursor));
	}

	@Override
	public int getCount() {
		return datesCursor.getCount();
	}
}
