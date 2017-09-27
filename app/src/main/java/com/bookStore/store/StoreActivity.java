package com.bookStore.store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.*;
import android.widget.ListView;
import android.widget.TextView;

import com.bookStore.App.CursorSearchAdapter;
import com.bookStore.App.DataBase;
import com.bookStore.Calculation.CalcActivity;
import com.bookStore.Persons.PersonsActivity;
import com.bookStore.Preference.BackupAndRestore;
import com.bookStore.Preference.ExportBooksToCSV;
import com.bookStore.Preference.PrefActivity;
import com.bookStore.R;
import com.bookStore.Reports.JointActivity;
import com.bookStore.ui.BaseListActivityOld;
import com.bookStore.ui.SaleActivity;

public class StoreActivity extends BaseListActivityOld<StoreActivity.ListAdapter> {

	private static LayoutInflater lInflater;
	private BookDetailDialog bookDetail;

	public static LayoutInflater getLInflater() {
		return lInflater;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store);
		setListAdapter(new ListAdapter());
		lInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		getListView().requestFocusFromTouch();
		startActivity(new Intent(this, SaleActivity.class));
	}

	@Override
	public boolean onCreateOptionsMenu(@NonNull Menu menu) {
		getMenuInflater().inflate(R.menu.activity_store, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.menu_sale) {
			startActivity(new Intent(this, SaleActivity.class));
		} else if (id == R.id.menu_store_persons) {
			startActivity(new Intent(this, PersonsActivity.class));
		} else if (id == R.id.store_menu_calculate) {
			startActivity(new Intent(this, CalcActivity.class));
		} else if (id == R.id.store_menu_reports) {
			startActivity(new Intent(this, JointActivity.class));
		} else if (id == R.id.store_menu_preference) {
			startActivity(new Intent(this, PrefActivity.class));
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

	class ListAdapter extends CursorSearchAdapter {

		@Override
		public String getQuery() {
			return "SELECT _id, bookName, shortName, cost, count FROM books ORDER BY sort";
		}

		@Override
		public String getQuery(String search) {
			return "SELECT _id, bookName, shortName, cost, count" +
					" FROM books" +
					" WHERE sort LIKE '" + search.toLowerCase() + "%'" +
					" or sort LIKE '% " + search.toLowerCase() + "%'" +
					" ORDER BY  sort";
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			cursor.moveToPosition(position);
			ViewHolder holder;
			if (convertView == null) {
				convertView = StoreActivity.getLInflater().inflate(R.layout.item_store, parent, false);
				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.bookName);
				holder.cost = (TextView) convertView.findViewById(R.id.cost);
				convertView.setTag(holder);
			} else
				holder = (ViewHolder) convertView.getTag();

			holder.name.setText(cursor.getString(1));
			holder.cost.setText(cursor.getString(3) + "р.");
			return convertView;
		}
	}
}
