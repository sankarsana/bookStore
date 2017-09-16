package com.bookStore.Cards;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bookStore.App.CursorAdapter;
import com.bookStore.R;
import com.bookStore.Store.StoreActivity;

public class CardEntriesAdapter extends CursorAdapter {

	protected long dateId;
	private long personId;

	public CardEntriesAdapter(long personId, long dateId) {
		super();
		this.personId = personId;
		this.dateId = dateId;
	}

	@Override
	public String getQuery() {
		return "SELECT" +
				" Books._id AS _id," +
				" Books.bookName AS bookName," +
				" Books.shortName AS shortName," +
				" InStock.count AS count," +

				" (SELECT CardEntries.get FROM CardEntries" +
				" WHERE CardEntries.dateId = '" + dateId + "'" +
				" AND CardEntries.bookId = Books._id) AS get," +

				" (SELECT CardEntries.distr FROM CardEntries" +
				" WHERE CardEntries.dateId = '" + dateId + "'" +
				" AND CardEntries.bookId = Books._id) AS distr," +

				" (SELECT CardEntries.ret FROM CardEntries" +
				" WHERE CardEntries.dateId = '" + dateId + "'" +
				" AND CardEntries.bookId = Books. _id) AS ret," +

				" (SELECT CardEntries._id FROM CardEntries" +
				" WHERE CardEntries.dateId = '" + dateId + "'" +
				" AND CardEntries.bookId = Books._id) AS cardEntryId" +

				" FROM Books, InStock" +
				" WHERE InStock.bookId = Books._id" +
				" AND InStock.personId = '" + personId + "'" +
				" ORDER BY bookName";
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (convertView == null)
			view = StoreActivity.getLInflater().inflate(
					R.layout.card_item_person, parent, false);
		cursor.moveToPosition(position);
		CardEntry entry = new CardEntry(cursor);
		((TextView) view.findViewById(R.id.shortName)).setText(entry.shortName);
		((TextView) view.findViewById(R.id.bookName)).setText(cursor.getString(1));
		((TextView) view.findViewById(R.id.cardBookCount)).setText(entry.getCountString());
		((TextView) view.findViewById(R.id.cardGet)).setText(
				entry.get == 0 ? "" : entry.getGetString());
		((TextView) view.findViewById(R.id.cardDistr)).setText(
				entry.distr == 0 ? "" : entry.getDistrString());
		((TextView) view.findViewById(R.id.cardRet)).setText(
				entry.ret == 0 ? "" : entry.getRetString());
		return view;
	}

	protected String getBookNameFormat(CardEntry entry) {
		return entry.shortName.length() != 0 ? entry.shortName : entry.bookName;
	}
}
