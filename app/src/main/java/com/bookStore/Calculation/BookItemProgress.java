package com.bookStore.Calculation;

import android.app.ListActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bookStore.R;

public class BookItemProgress extends BaseAdapter {

	private static BookItemProgress bookItemProgress;
	private final String newEntry = "< Новая запись >";
	private LayoutInflater lInflater;
	private String[] progressArray;
	private ListView listView;

	private BookItemProgress(ListActivity listActivity, String progressString) {
		this.listView = listActivity.getListView();
		lInflater = (LayoutInflater) listActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		progressString = (progressString == null)
				? newEntry : progressString + " + " + newEntry;
		progressArray = progressString.split("\\+");
	}

	public static BookItemProgress initialise(ListActivity listActivity, String progressString) {
		return bookItemProgress = new BookItemProgress(listActivity, progressString);
	}

	public static BookItemProgress getInstance() {
		return bookItemProgress;
	}

	public void setProgressEntry(int position, String entry) {
		progressArray[position] = entry;
	}

	public void saveData() {
		String progress = "";
		int count = 0;
		for (String entry : progressArray) {
			entry = entry.trim();
			if (!entry.equals(newEntry)) {
				progress = progress + " + " + entry;
				count += Integer.parseInt(entry);
			}
		}
		BookItemsAdapter.getInstance().updateCurrentItem(progress.substring(3), count);
	}

	@Override
	public int getCount() {
		return progressArray.length;
	}

	@Override
	public String getItem(int i) {
		return progressArray[i];
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		View v = view;
		if (view == null)
			v = lInflater.inflate(R.layout.progress_item, viewGroup, false);
		((TextView) v.findViewById(R.id.progressItemText)).setText(progressArray[i].trim());

		if (listView.getCheckedItemPosition() == i)
			v.setBackgroundResource(R.drawable.select_vertical);
		else
			v.setBackgroundResource(R.drawable.solid_white);
		return v;
	}
}
