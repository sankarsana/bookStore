package com.bookStore.gateway

import android.content.Context
import com.bookStore.App.DataBase
import com.bookStore.model.Book
import com.bookStore.model.BookImpl

class GatewayImpl(context: Context) : Gateway {

	private var selectedBook: Book? = null

	init {
		DataBase.initialize(context)
	}

	override fun pushSelected(book: Book) {
		selectedBook = book
	}

	override fun popSelected(): Book? {
		val returnValue: Book? = selectedBook
		selectedBook = null
		return returnValue
	}

	override fun fetchBookList(searchText: String): List<Book> {
		val where = if (searchText.isEmpty()) "" else {
			"WHERE sort LIKE '$searchText%' OR sort LIKE '% $searchText%'"
		}
		val query = "SELECT _id, bookName, shortName, count, cost FROM books $where ORDER BY sort"
		val cursor = DataBase.get().rawQuery(query, null)
		val books = ArrayList<Book>()
		while (cursor.moveToNext()) {
			val book = BookImpl()
			book.bookName = cursor.getString(1)
			book.count = cursor.getInt(3)
			book.cost = cursor.getInt(4)
			books.add(book)
		}
		cursor.close()
		return books
	}
}