package com.bookStore.store;

import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.bookStore.App.DataBase;
import com.bookStore.App.MyDialogFragment;
import com.bookStore.Persons.UpdateDialog;
import com.bookStore.R;

public class WriterChoiceFragment extends MyDialogFragment {

	private ListView listView;
	private Adapter adapter;
	private LayoutInflater inflater;
	private IDialogItemClick click;

	public WriterChoiceFragment(IDialogItemClick click) {
		this.click = click;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		View view = inflater.inflate(R.layout.publisher_choice, container, false);
		listView = (ListView) view.findViewById(android.R.id.list);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				click.onDialogItemClick(adapter.getItem(position));
				dismiss();
			}
		});

		view.findViewById(R.id.addWriter).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UpdateDialog newWriterDialog = new UpdateDialog();
				newWriterDialog.addData("title", "Новый издатель");
				newWriterDialog.show(getFragmentManager(), "newWriterDialog");
				dismiss();
			}
		});
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		adapter = new Adapter();
		listView.setAdapter(adapter);
	}

	/**
	 * Adapter
	 */

	private class Adapter extends BaseAdapter {

		private Cursor cursor;

		private Adapter() {
			cursor = DataBase.get().rawQuery("SELECT * FROM writers ORDER BY writer", null);
		}

		@Override
		public int getCount() {
			return cursor.getCount();
		}

		@Override
		public Writer getItem(int position) {
			cursor.moveToPosition(position);
			return new Writer(cursor.getLong(0), cursor.getString(1));
		}

		@Override
		public long getItemId(int position) {
			cursor.moveToPosition(position);
			return cursor.getLong(0);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null)
				convertView = inflater.inflate(R.layout.writer_item, parent, false);

			cursor.moveToPosition(position);
			((TextView) convertView.findViewById(R.id.writer)).setText(cursor.getString(1));
			return convertView;
		}
	}
}
