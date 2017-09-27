package com.bookStore.Cards;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bookStore.Persons.Person;
import com.bookStore.R;
import com.bookStore.ui.StoreActivity;

public class CardEntriesAdapter_store extends CardEntriesAdapter {

	public CardEntriesAdapter_store(long dateId) {
		super(Person.STORE_ID, dateId);
	}

	@Override
	public String getQuery() {
		return "SELECT" +
				" books._id AS _id," +
				" books.bookName AS bookName," +
				" books.shortName AS shortName," +
				"'0' AS count, " +

				"'0' as get, " +
				"CardEntries.distr AS distr, " +
				"'0' AS ret, " +
				"CardEntries._id AS cardEntryId " +

				" FROM books, CardEntries" +
				" WHERE  CardEntries.dateId = '" + dateId + "'" +
				" AND books._id = CardEntries.bookId" +
				" ORDER BY bookName";
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (convertView == null)
			view = StoreActivity.Companion.getLInflater().inflate(R.layout.item_card_store, parent, false);
		cursor.moveToPosition(position);
		CardEntry entry = new CardEntry(cursor);
		((TextView) view.findViewById(R.id.shortName))
				.setText(getBookNameFormat(entry));
		((TextView) view.findViewById(R.id.cardDistr)).setText(
				entry.distr == 0 ? "" : entry.getDistrString());
		return view;
	}
}
