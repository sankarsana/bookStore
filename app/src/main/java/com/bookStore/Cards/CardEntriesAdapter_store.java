package com.bookStore.Cards;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bookStore.Persons.Person;
import com.bookStore.R;
import com.bookStore.Store.StoreActivity;

public class CardEntriesAdapter_store extends CardEntriesAdapter {

	public CardEntriesAdapter_store(long dateId) {
		super(Person.STORE_ID, dateId);
	}

	@Override
	public String getQuery() {
		return "SELECT" +
				" Books._id AS _id," +
				" Books.bookName AS bookName," +
				" Books.shortName AS shortName," +
				"'0' AS count, " +

				"'0' as get, " +
				"CardEntries.distr AS distr, " +
				"'0' AS ret, " +
				"CardEntries._id AS cardEntryId " +

				" FROM Books, CardEntries" +
				" WHERE  CardEntries.dateId = '" + dateId + "'" +
				" AND Books._id = CardEntries.bookId" +
				" ORDER BY bookName";
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (convertView == null)
			view = StoreActivity.getLInflater().inflate(R.layout.card_item_store, parent, false);
		cursor.moveToPosition(position);
		CardEntry entry = new CardEntry(cursor);
		((TextView) view.findViewById(R.id.shortName))
				.setText(getBookNameFormat(entry));
		((TextView) view.findViewById(R.id.cardDistr)).setText(
				entry.distr == 0 ? "" : entry.getDistrString());
		return view;
	}
}
