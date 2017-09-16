package com.bookStore.Calculation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bookStore.App.App;
import com.bookStore.App.DataBase;
import com.bookStore.R;

import static android.widget.Toast.LENGTH_SHORT;

public class BookItemsAdapter extends BaseAdapter {

	public static BookItemsAdapter bookItemsAdapter;
	private Cursor cursor;
	private LayoutInflater lInflater;
	private int currentPosition;
	private SQLiteDatabase database = DataBase.get();

	private BookItemsAdapter(Context context) {
		super();
		lInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		refresh();
	}

	public static BookItemsAdapter getInstance() {
		return bookItemsAdapter;
	}

	public static BookItemsAdapter initialize(Context context) {
		return bookItemsAdapter = new BookItemsAdapter(context);
	}

	public void setCurrentPosition(int position) {
		currentPosition = position;
	}

	private void refresh() {
		cursor = database.rawQuery("SELECT" +
				" Calc._id AS _id," +
				" Calc.bookId AS bookId," +
				" Books.bookName AS bookName," +
				" Books.shortName AS shortName," +
				" Calc.progress AS progress," +
				" Calc.previousProgress AS previousProgress," +
				" CalC.count AS count," +
				" Calc.previousCount AS previousCount" +
				" FROM Calc, Books" +
				" WHERE Calc.bookId = Books. _id", null);
		notifyDataSetChanged();
	}

	public void clearData() {
		clearDataTable();
		addAllBooks();
	}

	@Override
	public int getCount() {
		return cursor.getCount();
	}

	@Override
	public BookItem getItem(int i) {
		cursor.moveToPosition(i);
		return new BookItem(cursor);
	}

	@Override
	public long getItemId(int position) {
		cursor.moveToPosition(position);
		return cursor.getLong(0);
	}

	public long getItemId() {
		return cursor.getLong(0);
	}

	public String getProgress() {
		cursor.moveToPosition(currentPosition);
		return cursor.getString(cursor.getColumnIndex("progress"));
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		View v = view;
		if (v == null) {
			v = lInflater.inflate(R.layout.calc_item, viewGroup, false);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.bookName = (TextView) v.findViewById(R.id.calc_bookName);
			viewHolder.progress = (TextView) v.findViewById(R.id.calc_progress);
			viewHolder.previousProgress = (TextView) v.findViewById(R.id.calc_progress_previous);
			viewHolder.count = (TextView) v.findViewById(R.id.calc_count);
			viewHolder.previousCount = (TextView) v.findViewById(R.id.calc_count_previous);
			v.setTag(viewHolder);
		}
		BookItem bookItem = getItem(i);
		ViewHolder viewHolder = (ViewHolder) v.getTag();
		viewHolder.bookName.setText(bookItem.bookName);
		viewHolder.progress.setText(bookItem.progressString);
		viewHolder.previousProgress.setText(bookItem.previousProgressString);
		viewHolder.count.setText(bookItem.count + "");
		viewHolder.previousCount.setText(bookItem.previousCount + "");
		return v;
	}

	private void clearDataTable() {
		database.delete("Calc", null, null);
	}

	public void addAllBooks() {
		Cursor bookIds = database.rawQuery("SELECT _id FROM Books", null);
//        ContentValues values = new ContentValues(1);
		if (bookIds.getCount() == 0) {
			Toast.makeText(App.getContext(), "No books!", LENGTH_SHORT).show();
			return;
		}
		database.beginTransaction();
		bookIds.moveToFirst();
		do {
			database.execSQL("INSERT INTO Calc (bookId) VALUES (" + bookIds.getInt(0) + ")");
		} while (bookIds.moveToNext());

//        for (int n = 0; n < bookIds.getCount(); n++) {
//            values.clear();
//            bookIds.moveToPosition(n);
//            values.put("bookId", bookIds.getLong(0));
//            database.insert("Calc", "bookId", values);
//        }
		database.setTransactionSuccessful();
		database.endTransaction();
	}

	public void updateCurrentItem(String progress, int count) {
		cursor.moveToPosition(currentPosition);
		ContentValues values = new ContentValues();
		values.put("progress", progress);
		values.put("count", count);
		database.update("Calc", values, "_id = " + getItemId(), null);
		refresh();
	}

	public void moveCurrentToPreviousAndClearCurrent() {
		database.beginTransaction();
		database.execSQL("UPDATE Calc SET previousProgress = progress");
		database.execSQL("UPDATE Calc SET progress = NULL");
		database.execSQL("UPDATE Calc SET previousCount = count");
		database.execSQL("UPDATE Calc SET count = '0'");
		database.setTransactionSuccessful();
		database.endTransaction();
		refresh();
	}

	public void movePreviousToCurrentAndClearPrevious() {
		database.beginTransaction();
		database.execSQL("UPDATE Calc SET progress = previousProgress ");
		database.execSQL("UPDATE Calc SET previousProgress = NULL");
		database.execSQL("UPDATE Calc SET count = previousCount");
		database.execSQL("UPDATE Calc SET previousCount = '0'");
		database.setTransactionSuccessful();
		database.endTransaction();
		refresh();
	}

	public void updateCountBooks() {
		database.execSQL("UPDATE Books SET count =" +
				" (SELECT Calc.count FROM Calc WHERE Calc.bookId = Books._id)");

	}

	static class ViewHolder {
		public TextView bookName;
		public TextView progress;
		public TextView previousProgress;
		public TextView count;
		public TextView previousCount;
	}
}
