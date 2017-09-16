package com.bookStore.App;

import android.database.Cursor;
import android.widget.BaseAdapter;

public abstract class CursorAdapter extends BaseAdapter {

	protected Cursor cursor;

	public CursorAdapter() {
		super();
		refresh();
	}

	public abstract String getQuery();

	public void refresh() {
		cursor = DataBase.get().rawQuery(getQuery(), null);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return cursor.getCount();
	}

	@Override
	public long getItemId(int position) {
		cursor.moveToPosition(position);
		return cursor.getLong(0);
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		cursor.close();
	}
}
