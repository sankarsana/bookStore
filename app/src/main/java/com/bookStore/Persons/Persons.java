package com.bookStore.Persons;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bookStore.App.CursorSearchAdapter;
import com.bookStore.App.DataBase;
import com.bookStore.Cards.CardDate;
import com.bookStore.R;
import com.bookStore.ui.StoreActivity;

public class Persons extends CursorSearchAdapter {

	private SQLiteDatabase db = DataBase.get();

	public Persons() {
		refresh();
	}

	public Person getPerson(int position) {
		cursor.moveToPosition(position);
		return new Person(cursor.getLong(0), cursor.getString(1),
				cursor.getInt(2));
	}

	public Person getPerson(long id) {
		return Person.getPerson(id);
	}

	public void addPersonsInPopupMenu(Menu menu) {
		cursor.moveToFirst();
		do {
			menu.add(0, cursor.getInt(0), Menu.NONE, cursor.getString(1));
		} while (cursor.moveToNext());
	}

	public long insert(Person person) {
		ContentValues values = new ContentValues();
		values.put("name", person.name);
		values.put("percent", person.percent);
		person.id = db.insert("Persons", "name", values);
		refresh();
		return person.id;
	}

	public void update(Person person) {
		ContentValues values = new ContentValues();
		values.put("name", person.name);
		values.put("percent", person.percent);
		db.update("Persons", values, "_id = " + person.id, null);
		refresh();
	}

	public void delete(long personId) {
		Cursor datesCursor = db.rawQuery(
				"SELECT _id FROM CardDate WHERE personId = " + personId, null);
		db.beginTransaction();
		if (datesCursor.moveToFirst()) {
			do {
				CardDate cardDate = new CardDate(datesCursor.getLong(0));
				cardDate.clearTransactionFree();
				db.delete("CardDate", "_id = " + datesCursor.getLong(0), null);
			} while (datesCursor.moveToNext());
		}
		db.delete("Persons", "_id = " + personId, null);
		db.delete("InStock", "personId = " + personId, null);
		db.setTransactionSuccessful();
		db.endTransaction();
		datesCursor.close();
	}

	@Override
	public String getQuery() {
		return "SELECT * FROM Persons ORDER BY name";
	}

	@Override
	public String getQuery(String search) {
		String searchBig = search.substring(0, 1).toUpperCase() + search.substring(1);
		return "SELECT * FROM Persons" +
				" WHERE name LIKE '" + search + "%' OR name LIKE '" + searchBig + "%'" +
				" OR name LIKE '% " + search + "%' OR name LIKE '% " + searchBig + "%'" +
				" ORDER BY name";
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (convertView == null)
			view = StoreActivity.Companion.getLInflater().inflate(R.layout.item_person, parent, false);
		Person person = getPerson(position);
		String percent = person.percent == 100 ? "" : Integer.toString(person.percent) + "%";
		((TextView) view.findViewById(R.id.personName)).setText(person.name);
		((TextView) view.findViewById(R.id.personPercent)).setText(percent);
		return view;
	}
}
