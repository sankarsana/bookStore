package com.bookStore.Import;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleCursorAdapter;

import com.bookStore.App.DataBase;
import com.bookStore.R;
import com.bookStore.store.Book;
import com.bookStore.store.Store;

public class ImportDetailAdapter extends SimpleCursorAdapter {

	private Cursor cursor;
	private ImportDates importDates;
	private SQLiteDatabase db = DataBase.get();

	@SuppressWarnings("deprecation")
	public ImportDetailAdapter(Context context) {
		super(context, R.layout.import_item, null,
				new String[]{"bookName", "count"},
				new int[]{R.id.addBook_name, R.id.addBook_count});
		importDates = new ImportDates();
	}

	public void setDate(long dateId) {
		if (dateId == -1)
			importDates.setCurrentDate_insertIfNotExists();
		else
			importDates.moveToIdPosition(dateId);
		refreshCursor();
	}

	public String getDateStringSimple() {
		return importDates.getDateStringSimple();
	}

	public String getDateStringExtend() {
		return importDates.getDateStringExtend();
	}

	public boolean delete_importDetail(int position) {
		getCursor().moveToPosition(position);
		if (new Book(getBookId()).count < getBookCount())
			return false;
		db.beginTransaction();
		db.delete("ImportDetail", "_id = " + getImportDetailId(), null);
		Store.updateBookCount(getBookId(), -getBookCount());
		db.setTransactionSuccessful();
		db.endTransaction();
		refreshCursor();
		return true;
	}

	public long addNewEntryAndUpdateBooksCount(long bookId, int count) {
		ContentValues values = new ContentValues();
		values.put("dateId", importDates.getDateId());
		values.put("bookId", bookId);
		values.put("count", count);
		db.beginTransaction();
		long detailId = db.insert("ImportDetail", "dateId", values);
		if (detailId != -1)
			Store.updateBookCount(bookId, count);
		db.setTransactionSuccessful();
		db.endTransaction();
		refreshCursor();
		return detailId;
	}

	public int delete_CurrentDateIfItIsEmpty() {
		if (!cursor.moveToFirst())
			return importDates.deleteDate();
		else
			return 0;
	}

	private long getBookId() {
		return cursor.getLong(cursor.getColumnIndex("bookId"));
	}

	private int getBookCount() {
		return cursor.getInt(cursor.getColumnIndex("count"));
	}

	private long getImportDetailId() {
		return cursor.getLong(cursor.getColumnIndex("_id"));
	}

	private void refreshCursor() {
		cursor = db.rawQuery("SELECT ImportDetail._id AS _id, "
				+ "ImportDetail.dateId AS dateId, "
				+ "books._id AS bookId, "
				+ "books.bookName AS bookName, "
				+ "ImportDetail.count AS count "
				+ "FROM ImportDetail, books "
				+ "WHERE ImportDetail.bookId = books._id AND ImportDetail.dateId = "
				+ importDates.getId(), null);
		changeCursor(cursor);
	}
}
