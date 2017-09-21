package com.bookStore.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.ListView;
import android.widget.TextView;

import com.bookStore.App.BaseListActivity;
import com.bookStore.App.CursorSearchAdapter;
import com.bookStore.R;

public class SelectBookActivity extends BaseListActivity {

	public static final String NEW_BOOK = "newBook";
	private SelectBookAdapter adapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_book_act);
//		toolbar.setDisplayHomeAsUpEnabled(true);
		adapter = new SelectBookAdapter();
		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.select_book, menu);
		if (getIntent().getBooleanExtra(NEW_BOOK, false))
			menu.getItem(1).setVisible(false);
		initializeMySearchView(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.selectBook_add) {
			Intent intent = new Intent(SelectBookActivity.this, UpdateBookAct.class);
			startActivityForResult(intent, 1);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		getIntent().putExtra("bookId", id);
		getIntent().putExtra("bookName", adapter.getName(position));
		setResult(RESULT_OK, getIntent());
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			setResult(RESULT_OK, data);
			finish();
		}
	}

	private class SelectBookAdapter extends CursorSearchAdapter {

		@Override
		public String getQuery() {
			return "SELECT _id, bookName, shortName, count" +
					" FROM books ORDER BY bookName";
		}

		@Override
		public String getQuery(String search) {
			return "SELECT _id, bookName, shortName, count" +
					" FROM books" +
					" WHERE search LIKE '% " + search + "%'" +
					" ORDER BY  bookName";
		}

		private String getName(int position) {
			cursor.moveToPosition(position);
			return cursor.getString(1);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (convertView == null)
				view = StoreActivity.getLInflater().inflate(R.layout.select_book_item, parent, false);
			cursor.moveToPosition(position);
			((TextView) view.findViewById(R.id.selectBook_name)).setText(cursor.getString(1));
			((TextView) view.findViewById(R.id.selectBook_count)).setText(cursor.getString(3));
			return view;
		}
	}
}
