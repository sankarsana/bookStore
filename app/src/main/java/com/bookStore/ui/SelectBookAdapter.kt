package com.bookStore.ui

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bookStore.App.CursorSearchAdapter
import com.bookStore.R
import com.bookStore.model.Book
import com.bookStore.model.BookImpl
import com.bookStore.store.StoreActivity

class SelectBookAdapter : CursorSearchAdapter() {

	override fun getQuery(): String =
			"SELECT _id, bookName, shortName, count, cost" + " FROM books ORDER BY bookName"

	override fun getQuery(search: String): String {
		return "SELECT _id, bookName, shortName, count, cost" +
				" FROM books" +
				" WHERE sort LIKE '% $search%'" +
				" ORDER BY  bookName"
	}

	fun getName(position: Int): String {
		cursor.moveToPosition(position)
		return cursor.getString(1)
	}

	override fun getItem(position: Int): Book {
		cursor.moveToPosition(position)
		val book = BookImpl()
		book.bookName = cursor.getString(1)
		book.cost = cursor.getInt(4)
		return book
	}

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		var view = convertView
		if (convertView == null)
			view = StoreActivity.getLInflater().inflate(R.layout.select_book_item, parent, false)
		cursor.moveToPosition(position)
		(view?.findViewById(R.id.selectBook_name) as TextView).text = cursor.getString(1)
		(view.findViewById(R.id.selectBook_count) as TextView).text = cursor.getString(3)
		return view
	}
}