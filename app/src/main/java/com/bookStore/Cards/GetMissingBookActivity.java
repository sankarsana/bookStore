package com.bookStore.Cards;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.*;
import android.widget.*;

import com.bookStore.App.ActionBarListActivity;
import com.bookStore.App.DataBase;
import com.bookStore.Persons.PersonsActivity;
import com.bookStore.R;

public class GetMissingBookActivity extends ActionBarListActivity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiple_select_act);

//		toolbar.setDisplayHomeAsUpEnabled(true);
		getListView().setAdapter(new ItemsAdapter());
		findViewById(R.id.btnOk).setOnClickListener(this);
		findViewById(R.id.btnCancel).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.get_missing, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		((ItemsAdapter) getListAdapter()).selectAll();
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnOk)
			((ItemsAdapter) getListAdapter()).exGetBooks();
		finish();
	}

	/**
	 * ---------------------- Adapter --------------------------------------------------------------
	 */
	static class ViewHolder {
		TextView bookName;
		TextView writer;
		CheckBox checkBox;
	}

	/**
	 * --------------------- Item ------------------------------------------------------------------
	 */
	private class Item {

		private String bookName;
		private String writer;
		private boolean checked;

		private Item(String bookName, String writer, boolean checked) {
			this.bookName = bookName;
			this.writer = writer;
			this.checked = checked;
		}
	}

	private class ItemsAdapter extends BaseAdapter {

		private Cursor cursor;
		private SparseBooleanArray myChecked = new SparseBooleanArray();
		private boolean selectAll = false;

		private ItemsAdapter() {
			refreshCursor();
			setMyChecked(true);

			getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					selectAll = true;
					myChecked.put(position, !myChecked.get(position));
					((ViewHolder) view.getTag()).checkBox.setChecked(myChecked.get(position));
				}
			});
		}

		private void setMyChecked(boolean check) {
			for (int i = 0; i < cursor.getCount(); i++) {
				myChecked.put(i, check);
			}
		}

		private void refreshCursor() {
			cursor = DataBase.get().rawQuery(
					"SELECT books._id, books.bookName, writers.writer" +
							" FROM books" +
							" LEFT OUTER JOIN InStock ON books._id = InStock.bookId" +
							" AND InStock.personId = " + PersonsActivity.getPerson().id +
							" INNER JOIN writers ON books.writerId = writers._id" +
							" WHERE InStock.count IS NULL OR InStock.count = '' OR InStock.count = '0'", null
			);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return cursor.getCount();
		}

		@Override
		public Item getItem(int position) {
			cursor.moveToPosition(position);
			return new Item(cursor.getString(1), cursor.getString(2), myChecked.get(position));
		}

		@Override
		public long getItemId(int position) {
			cursor.moveToPosition(position);
			return cursor.getLong(0);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Item item = getItem(position);
			final ViewHolder viewHolder;
			if (convertView == null) {
				convertView = getLayoutInflater()
						.inflate(R.layout.multiple_select_item, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.bookName = (TextView) convertView.findViewById(R.id.bookName_multiSelect);
				viewHolder.writer = (TextView) convertView.findViewById(R.id.writer_multiSelect);
				viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox_multiSelect);
				convertView.setTag(viewHolder);
			} else
				viewHolder = (ViewHolder) convertView.getTag();

			viewHolder.bookName.setText(item.bookName);
			viewHolder.writer.setText(item.writer);
			viewHolder.checkBox.setChecked(item.checked);
			return convertView;
		}

		public void exGetBooks() {
			if (cursor.getCount() == 0)
				return;
			cursor.moveToFirst();
			CardDate cardDate = CardDateFragment.getCardDate();
			SQLiteDatabase db = DataBase.get();
			db.beginTransaction();
			do {
				if (myChecked.get(cursor.getPosition()))
					cardDate.getBooksTransactionFree(cursor.getLong(0), 1);
			} while (cursor.moveToNext());
			db.setTransactionSuccessful();
			db.endTransaction();
		}

		public void selectAll() {
			setMyChecked(selectAll);
			notifyDataSetChanged();
			selectAll = !selectAll;
		}
	}
}
