package com.bookStore.Cards;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bookStore.App.BaseListActivity;
import com.bookStore.App.CursorSearchAdapter;
import com.bookStore.App.DataBase;
import com.bookStore.Persons.Person;
import com.bookStore.Persons.PersonsActivity;
import com.bookStore.R;
import com.bookStore.store.Book;
import com.bookStore.store.Store;
import com.bookStore.store.StoreActivity;

public class EditorActivity extends BaseListActivity
		implements EditorInputDialog.IEditorInputDialog {

	private EditorInputDialog inputDialog;
	private long selectedBookId;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookstore_selection);

		setToolbarTitle(CardDateFragment.getCardDate().getDateSimple_String());
		setToolbarSubtitle(getIntent().getStringExtra("modeTitle"));

		inputDialog = new EditorInputDialog();

		if (PersonsActivity.getPerson().id == Person.STORE_ID) {
			getListView().setAdapter(new PersonAdapterStore());
		} else {
			switch (getIntent().getIntExtra("mode", -1)) {
				case DistributorCardActivity.MODE_GET:
				case DistributorCardActivity.MODE_GET_AND_DISTR:
					getListView().setAdapter(new PersonAdapterGet());
					break;
				case DistributorCardActivity.MODE_DISTR:
					getListView().setAdapter(new PersonAdapterDistr());
					break;
				case DistributorCardActivity.MODE_RET:
					getListView().setAdapter(new PersonAdapterRet());
			}
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		selectedBookId = id;
		EditorAdapter editorAdapter = (EditorAdapter) getListAdapter();
		getIntent().putExtra("bookName", editorAdapter.getBookName(position));
		getIntent().putExtra("currentCount", editorAdapter.getCurrentCount(position));
		getIntent().putExtra("availableCount", editorAdapter.getAvailableCount(position));
		inputDialog.show(getSupportFragmentManager(), "input");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.editor, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean executeOk(int additionalCount) {
		int availableCount = getIntent().getIntExtra("availableCount", 0);
		CardDate cardDate = CardDateFragment.getCardDate();

		if (PersonsActivity.getPerson().id == Person.STORE_ID) {
			if (new Book(selectedBookId).count >= additionalCount) {
				DataBase.get().beginTransaction();
				cardDate.distribBooksTransactionFree(selectedBookId, additionalCount);
				Store.updateBookCount(selectedBookId, -additionalCount);
				DataBase.get().setTransactionSuccessful();
				DataBase.get().endTransaction();
			} else {
				Toast.makeText(this, "На складе нет такого количества!",
						Toast.LENGTH_SHORT).show();
				return false;
			}
		} else {

			switch (getIntent().getIntExtra("mode", -1)) {
				case DistributorCardActivity.MODE_GET:
					if (new Book(selectedBookId).count >= additionalCount)
						cardDate.getBooksInTransaction(selectedBookId, additionalCount);
					else {
						Toast.makeText(this, "На складе нет такого количества!",
								Toast.LENGTH_SHORT).show();
						return false;
					}
					break;
				case DistributorCardActivity.MODE_GET_AND_DISTR:
					if (new Book(selectedBookId).count >= additionalCount) {
						cardDate.getBooksInTransaction(selectedBookId, additionalCount);
						cardDate.distribBooksInTransaction(selectedBookId, additionalCount);
					} else {
						Toast.makeText(this, "На складе нет такого количества!",
								Toast.LENGTH_SHORT).show();
						return false;
					}
					break;
				case DistributorCardActivity.MODE_DISTR:
					if (additionalCount <= availableCount)
						cardDate.distribBooksInTransaction(selectedBookId, additionalCount);
					else {
						Toast.makeText(this, "У Вас нет столько книг!",
								Toast.LENGTH_SHORT).show();
						return false;
					}
					break;
				case DistributorCardActivity.MODE_RET:
					if (additionalCount <= availableCount)
						cardDate.returnBooksInTransaction(selectedBookId, additionalCount);
					else {
						Toast.makeText(this, "У Вас нет столько книг!", Toast.LENGTH_SHORT)
								.show();
						return false;
					}
					break;
			}
		}
		((EditorAdapter) getListAdapter()).refresh();
		return true;
	}


	/**
	 * Adapters
	 */

	private class PersonAdapterGet extends EditorAdapter {

		@Override
		public String getQuery(String search) {
			return " SELECT books._id AS _id," +
					" books.count," +
					" books.bookName," +
					" books.shortName," +
					" CardEntries.get," +
					" books.search AS search" +
					" FROM books LEFT OUTER JOIN CardEntries ON books._id = CardEntries.bookId" +
					" AND CardEntries.dateId = " + getDateId() +
					" WHERE search LIKE '% " + search + "%'" +
					" ORDER BY bookName";
		}

		@Override
		public String getQuery() {
			return " SELECT books._id AS _id," +
					" books.count," +
					" books.bookName," +
					" books.shortName," +
					" CardEntries.get" +
					" FROM books LEFT OUTER JOIN CardEntries ON books._id = CardEntries.bookId" +
					" AND CardEntries.dateId = " + getDateId() +
					" ORDER BY bookName";
		}
	}

	private class PersonAdapterDistr extends EditorAdapter {

		@Override
		public String getQuery(String search) {
			return " SELECT books._id AS _id," +
					" InStock.count AS amount," +
					" books.bookName," +
					" books.shortName," +
					" CardEntries.distr," +
					" books.search AS search" +
					" FROM books " +
					" INNER JOIN InStock ON books._id = InStock.bookId" +
					" LEFT OUTER JOIN CardEntries ON books._id = CardEntries.bookId" +
					" AND CardEntries.dateId = " + getDateId() +
					" WHERE InStock.personId = " + getPersonId() + " AND amount <> '' AND amount <> '0'" +
					" AND search LIKE '% " + search + "%'" +
					" ORDER BY bookName";
		}

		@Override
		public String getQuery() {
			return " SELECT books._id AS _id," +
					" InStock.count AS amount," +
					" books.bookName," +
					" books.shortName," +
					" CardEntries.distr" +
					" FROM books " +
					" INNER JOIN InStock ON books._id = InStock.bookId" +
					" LEFT OUTER JOIN CardEntries ON books._id = CardEntries.bookId" +
					" AND CardEntries.dateId = " + getDateId() +
					" WHERE InStock.personId = " + getPersonId() + " AND amount <> '' AND amount <> '0'" +
					" ORDER BY bookName";
		}
	}

	private class PersonAdapterRet extends EditorAdapter {

		@Override
		public String getQuery(String search) {
			return " SELECT books._id AS _id," +
					" InStock.count AS amount," +
					" books.bookName," +
					" books.shortName," +
					" CardEntries.ret," +
					" books.search AS search" +
					" FROM books " +
					" INNER JOIN InStock ON books._id = InStock.bookId" +
					" LEFT OUTER JOIN CardEntries ON books._id = CardEntries.bookId" +
					" AND CardEntries.dateId = " + getDateId() +
					" WHERE InStock.personId = " + getPersonId() + " AND amount <> '' AND amount <> '0'" +
					" AND search LIKE '% " + search + "%'" +
					" ORDER BY bookName";
		}

		@Override
		public String getQuery() {
			return " SELECT books._id AS _id," +
					" InStock.count AS amount," +
					" books.bookName," +
					" books.shortName," +
					" CardEntries.ret" +
					" FROM books " +
					" INNER JOIN InStock ON books._id = InStock.bookId" +
					" LEFT OUTER JOIN CardEntries ON books._id = CardEntries.bookId" +
					" AND CardEntries.dateId = " + getDateId() +
					" WHERE InStock.personId = " + getPersonId() + " AND amount <> '' AND amount <> '0'" +
					" ORDER BY bookName";
		}
	}

	private class PersonAdapterStore extends EditorAdapter {

		@Override
		public String getQuery(String search) {
			return " SELECT books._id AS _id," +
					" books.count," +
					" books.bookName," +
					" books.shortName," +
					" CardEntries.distr," +
					" books.search AS search" +
					" FROM books LEFT OUTER JOIN CardEntries ON books._id = CardEntries.bookId" +
					" AND CardEntries.dateId = " + getDateId() +
					" WHERE search LIKE '% " + search + "%'" +
					" ORDER BY bookName";
		}

		@Override
		public String getQuery() {
			return " SELECT books._id AS _id," +
					" books.count," +
					" books.bookName," +
					" books.shortName," +
					" CardEntries.distr" +
					" FROM books LEFT OUTER JOIN CardEntries ON books._id = CardEntries.bookId" +
					" AND CardEntries.dateId = " + getDateId() +
					" ORDER BY bookName";
		}
	}

	private abstract class EditorAdapter extends CursorSearchAdapter {

		protected long getDateId() {
			return CardDateFragment.getCardDate().id;
		}

		protected long getPersonId() {
			return PersonsActivity.getPerson().id;
		}

		public int getCurrentCount(int position) {
			cursor.moveToPosition(position);
			return cursor.getInt(4);
		}

		public int getAvailableCount(int position) {
			cursor.moveToPosition(position);
			return cursor.getInt(1);
		}

		public String getBookName(int position) {
			cursor.moveToPosition(position);
			return cursor.getString(2);
		}

		@Override
		public Object getItem(int position) {
			return null;
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (convertView == null)
				view = StoreActivity.getLInflater()
						.inflate(R.layout.editor_list_item, parent, false);

			cursor.moveToPosition(position);
			((TextView) view.findViewById(R.id.editorListItem_availableCount))
					.setText(cursor.getString(1));
			((TextView) view.findViewById(R.id.editorListItem_shortName))
					.setText(cursor.getString(3));
			((TextView) view.findViewById(R.id.editorListItem_currentCount))
					.setText(cursor.getString(4));
			((TextView) view.findViewById(R.id.editorListItem_bookName))
					.setText(cursor.getString(2));

			if (getListView().getCheckedItemPosition() == position)
				view.setBackgroundResource(R.drawable.select_vertical);
			else
				view.setBackgroundResource(R.drawable.list_selector);

			return view;
		}
	}
}
