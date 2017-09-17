package com.bookStore.Cards;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bookStore.App.DataBase;
import com.bookStore.App.Utilities;
import com.bookStore.Persons.Person;
import com.bookStore.store.Store;

public class CardDate {

	long id;
	String dateString;
	long personId;
	int sum;
	String mark;
	private int percent;
	private SQLiteDatabase db = DataBase.get();

	public CardDate(long dateId) {
		id = dateId;
		refresh();
	}

	public CardDate(Cursor cursor) {
		initialize(cursor);
	}

	private void initialize(Cursor cursor) {
		id = cursor.getLong(0);
		dateString = cursor.getString(1);
		personId = cursor.getLong(2);
		sum = cursor.getInt(3);
		percent = cursor.getInt(4);
		mark = cursor.getString(5);
	}

	public void refresh() {
		Cursor cursor = db.rawQuery("SELECT * FROM CardDate WHERE _id  = " + id, null);
		cursor.moveToFirst();
		initialize(cursor);
		cursor.close();
	}

	public String getDateSimple_String() {
		return Utilities.convertDateDBToSimple(dateString);
	}

	public int getPercent() {
		return percent > 1000 ? percent - 1000 : percent;
	}

	public void setPercent(int value) {
		if (getConduct())
			value += 1000;
		db.execSQL("UPDATE CardDate SET percent = '" + value +
				"' WHERE _id = '" + id + "'");
		percent = value;
		updateSum();
	}

	public void setConductOpposite() {
		int newPercent = getConduct() ? getPercent() : getPercent() + 1000;
		db.execSQL("UPDATE CardDate SET percent = '" + newPercent +
				"' WHERE _id = '" + id + "'");
		percent = newPercent;
	}

	public boolean getConduct() {
		return percent > 1000;
	}

	public void setMark(String _mark) {
		db.execSQL("UPDATE CardDate SET mark = '" + _mark +
				"' WHERE _id = '" + id + "'");
		mark = _mark;
	}

	public void updateSum() {
		Cursor cur = db.rawQuery(
				"SELECT SUM(CardEntries.distr * books.cost) FROM CardEntries" +
						" INNER JOIN books ON CardEntries.bookId = books._id" +
						" WHERE CardEntries.dateId = '" + id + "'", null
		);
		int _sum = cur.moveToFirst() ? cur.getInt(0) : 0;
		cur.close();
		if (getPercent() != 100)
			_sum = (int) ((float) _sum * (float) getPercent() / 100F + 0.5F);
		db.execSQL("UPDATE CardDate SET sum = '" + _sum
				+ "' WHERE _id = " + id);
		sum = _sum;
	}

	public void clearInTransaction() {
		db.beginTransaction();
		clearTransactionFree();
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public void clearTransactionFree() {
		Cursor cursor = db.rawQuery("SELECT * FROM CardEntries WHERE dateId = " + id, null);
		if (!cursor.moveToFirst())
			return;
		long bookId;
		int value;
		do {
			bookId = cursor.getLong(2);
			value = cursor.getInt(3);
			if (value != 0)
				getBooksTransactionFree(bookId, -value);
			value = cursor.getInt(4);
			if (value != 0)
				distribBooksTransactionFree(bookId, -value);
			value = cursor.getInt(5);
			if (value != 0)
				returnBooksTransactionFree(bookId, -value);
			db.delete("CardEntries", "_id = " + cursor.getLong(0), null);
		} while (cursor.moveToNext());
		cursor.close();
	}


	/**
	 *
	 *
	 */

	//  ----- get -----
	public void getBooksTransactionFree(long bookId, int numberOfBooks) {
		long cardEntryId = getCardEntryId_InsertNewIfNotExists(bookId);

		db.execSQL("update CardEntries SET get = (get + ?) where _id = ?",
				new Object[]{numberOfBooks, cardEntryId});
		Store.updateBookCount(bookId, -numberOfBooks);
		updateInStockCountOrInsertIfNotExists(bookId, numberOfBooks);
	}

	public void getBooksInTransaction(long bookId, int numberOfBooks) {
		db.beginTransaction();
		getBooksTransactionFree(bookId, numberOfBooks);
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	private void updateInStockCountOrInsertIfNotExists(long bookId, int value) {
		Cursor c = db.rawQuery("SELECT count FROM InStock WHERE personId = "
				+ personId + " AND bookId = " + bookId, null);
		if (c.moveToFirst()) {
			if (c.getInt(0) + value == 0)
				db.execSQL("Update InStock set count = ''" +
						" Where personId = " + personId + " and bookId = " + bookId);
//                db.delete("InStock", "personId = " + personId + " AND bookId = " + bookId, null);
			else
				db.execSQL("Update InStock set count = count + " + value +
						" Where personId = " + personId + " and bookId = " + bookId);
		} else {
			ContentValues values = new ContentValues();
			values.put("personId", personId);
			values.put("bookId", bookId);
			values.put("count", value);
			db.insert("InStock", "personId", values);
		}
		c.close();
	}

	private long getCardEntryId_InsertNewIfNotExists(long bookId) {
		Cursor cursor = db.rawQuery("SELECT _id FROM CardEntries WHERE dateId = "
				+ id + " AND bookId = " + bookId, null);
		long newEntryId;
		if (cursor.moveToFirst()) {
			newEntryId = cursor.getLong(0);
		} else {
			ContentValues values = new ContentValues();
			values.put("dateId", id);
			values.put("bookId", bookId);
			values.put("get", 0);
			values.put("distr", 0);
			values.put("ret", 0);
			newEntryId = db.insert("CardEntries", "dateId", values);
		}
		cursor.close();
		return newEntryId;
	}

//  ----- distr -----

	public void distribBooksTransactionFree(long bookId, int numberOfBook) {
		if (numberOfBook == 0) return;
		long cardEntryId = getCardEntryId_InsertNewIfNotExists(bookId);
		db.execSQL("UPDATE CardEntries SET distr = (distr + ?) WHERE _id = ?",
				new Object[]{numberOfBook, cardEntryId});
		if (personId != Person.STORE_ID)
			updateInStockCountOrInsertIfNotExists(bookId, -numberOfBook);
		updateSum();
	}


	public void distribBooksInTransaction(long bookId, int numberOfBooks) {
		db.beginTransaction();
		distribBooksTransactionFree(bookId, numberOfBooks);
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public void distribAllBooks() {
		Cursor inStockCursor = db.rawQuery(
				"SELECT * FROM InStock WHERE personId = " + personId, null);
		if (!inStockCursor.moveToFirst()) return;
		db.beginTransaction();
		do {
			distribBooksTransactionFree(inStockCursor.getLong(1), inStockCursor.getInt(2));
		} while (inStockCursor.moveToNext());
		db.setTransactionSuccessful();
		db.endTransaction();
	}

//  ----- ret -----

	private void returnBooksTransactionFree(long bookId, int numberOfBooks) {
		if (numberOfBooks == 0) return;
		long cardEntryId = getCardEntryId_InsertNewIfNotExists(bookId);
		db.execSQL("UPDATE CardEntries SET ret = (ret + ?) WHERE _id = ?",
				new Object[]{numberOfBooks, cardEntryId});
		updateInStockCountOrInsertIfNotExists(bookId, -numberOfBooks);
		Store.updateBookCount(bookId, numberOfBooks);
	}

	public void returnBooksInTransaction(long bookId, int numberOfBooks) {
		db.beginTransaction();
		returnBooksTransactionFree(bookId, numberOfBooks);
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public void returnAllBooks() {
		Cursor inStockCursor = db.rawQuery(
				"SELECT * FROM InStock WHERE personId = " + personId, null);
		if (!inStockCursor.moveToFirst()) return;
		db.beginTransaction();
		do {
			returnBooksTransactionFree(inStockCursor.getLong(1), inStockCursor.getInt(2));
		} while (inStockCursor.moveToNext());
		db.setTransactionSuccessful();
		db.endTransaction();
	}
}

