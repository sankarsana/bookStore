package com.bookStore.store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.*;
import android.widget.ListView;
import android.widget.TextView;

import com.bookStore.App.ActionBarListActivity;
import com.bookStore.App.CursorSearchAdapter;
import com.bookStore.App.DataBase;
import com.bookStore.Calculation.CalcActivity;
import com.bookStore.Import.ImportActivity;
import com.bookStore.Import.ImportDates;
import com.bookStore.Persons.PersonsActivity;
import com.bookStore.Preference.BackupAndRestore;
import com.bookStore.Preference.ExportBooksToCSV;
import com.bookStore.Preference.PrefActivity;
import com.bookStore.R;
import com.bookStore.Reports.JointActivity;

public class StoreActivity extends ActionBarListActivity {

	private static final int MENU_HISTORY_GROUP = 123;
	private static LayoutInflater lInflater;
	private BookDetailDialog bookDetail;
	private SubMenu subMenu;

	public static LayoutInflater getLInflater() {
		return lInflater;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_act);
		setListAdapter(new ListAdapter());
		lInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		actionBar.setIcon(R.drawable.ic_launcher);
		actionBar.setDisplayShowCustomEnabled(true);

		getListView().requestFocusFromTouch();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (subMenu != null)
			createOptionsMenu();
	}

	@Override
	public void onCreateOverflowOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.store_over, menu);
		subMenu = menu.findItem(R.id.menu_bring_books).getSubMenu();
		createOptionsMenu();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.store_act_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public void createOptionsMenu() {
		subMenu.clear();
		ImportDates imDates = new ImportDates();
		if (imDates.getCount() != 0) {
			do {
				subMenu.add(MENU_HISTORY_GROUP, (int) imDates.getId(),
						Menu.NONE, imDates.getDateStringSimple());
			} while (imDates.moveToNext());
		}
		subMenu.add(MENU_HISTORY_GROUP, -1, Menu.NONE, "Новый привоз");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {


		if (item.getGroupId() == MENU_HISTORY_GROUP) {
			Intent intent = new Intent(this, ImportActivity.class);
			intent.putExtra("dateId", (long) item.getItemId());
			startActivity(intent);
			return super.onOptionsItemSelected(item);
		}
		switch (item.getItemId()) {

			case R.id.menu_store_persons:
				startActivity(new Intent(this, PersonsActivity.class));
				break;
			case R.id.store_menu_calculate:
				startActivity(new Intent(this, CalcActivity.class));
				break;
			case R.id.store_menu_reports:
				startActivity(new Intent(this, JointActivity.class));
				break;
			case R.id.store_menu_preference:
				startActivity(new Intent(this, PrefActivity.class));
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (bookDetail == null)
			bookDetail = new BookDetailDialog(this);
		bookDetail.show(id);
	}

	@Override
	protected void onDestroy() {
		if (bookDetail != null)
			bookDetail.dismiss();
		if (PreferenceManager.getDefaultSharedPreferences(this)
				.getBoolean("autoBackup", false)) {
			BackupAndRestore.backup();
			if (UpdateBookAct.isChanged) {
				ExportBooksToCSV exportBooksToCSV = new ExportBooksToCSV();
				exportBooksToCSV.autoExport();
				UpdateBookAct.isChanged = false;
			}
		}
		DataBase.closeDataBase();
		super.onDestroy();
	}

	private static class ViewHolder {
		public TextView name;
		public TextView shortName;
		public TextView count;
		public TextView cost;
	}

	private class ListAdapter extends CursorSearchAdapter {

		@Override
		public String getQuery() {
			return "SELECT _id, bookName, shortName, cost, count" +
					" FROM books ORDER BY  bookName";
		}

		@Override
		public String getQuery(String search) {
			return "SELECT _id, bookName, shortName, cost, count" +
					" FROM books" +
					" WHERE search LIKE '% " + search.toLowerCase() + "%'" +
					" ORDER BY  bookName";
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			cursor.moveToPosition(position);
			ViewHolder holder;
			if (convertView == null) {
				convertView = StoreActivity.getLInflater().inflate(R.layout.item_store, parent, false);
				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.bookName);
				holder.shortName = (TextView) convertView.findViewById(R.id.shortName);
				holder.count = (TextView) convertView.findViewById(R.id.count);
				holder.cost = (TextView) convertView.findViewById(R.id.cost);
				convertView.setTag(holder);
			} else
				holder = (ViewHolder) convertView.getTag();

			holder.name.setText(cursor.getString(1));
			String secondLine = cursor.getString(2);
			if (!secondLine.isEmpty()) secondLine = secondLine + "   ";
			holder.shortName.setText(secondLine + cursor.getString(4) + " шт.");
			holder.cost.setText(cursor.getString(3) + "р.");
			return convertView;
		}
	}
}
