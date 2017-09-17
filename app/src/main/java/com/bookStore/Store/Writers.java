package com.bookStore.Store;

import android.content.ContentValues;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bookStore.App.DataBase;
import com.bookStore.R;

public class Writers extends BaseAdapter {

	private static Writers instance = null;
	private Cursor cursor;

	public Writers() {
		super();
		refresh();
		notifyDataSetChanged();
	}

	public static Writers getInstance() {
		if (instance == null)
			instance = new Writers();
		return instance;
	}

	private void refresh() {
		cursor = DataBase.get().query(
				"writers", null, null, null, null, null, null);
	}

	public void insert(Writer writer) {
		ContentValues values = new ContentValues();
		values.put("writer", writer.getName());
		writer.setId(DataBase.get().insert("writers", "writer", values));
		refresh();
	}

	public void addWritersInPopupMenu(Menu menu) {
		cursor.moveToFirst();
		do {
			menu.add(0, cursor.getInt(0), Menu.NONE, cursor.getString(1));
		} while (cursor.moveToNext());
	}

	@Override
	public int getCount() {
		return cursor.getCount();
	}

	@Override
	public Writer getItem(int i) {
		cursor.moveToPosition(i);
		return new Writer(cursor.getLong(0), cursor.getString(1));
	}

	@Override
	public long getItemId(int i) {
		cursor.moveToPosition(i);
		return cursor.getLong(0);
	}

	public String getName() {
		return cursor.getString(1);
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		if (view == null)
			view = StoreActivity.getLInflater().inflate(R.layout.writer_item, viewGroup, false);
		((TextView) view.findViewById(R.id.writer)).setText(getName());
		return view;
	}
}
