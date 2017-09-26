package com.bookStore.gateway

import android.content.Context
import com.bookStore.App.DataBase
import com.bookStore.model.Book
import com.bookStore.model.BookImpl

class GatewayImpl(context: Context) : Gateway {

	override var selectedBook: Book = BookFake()

	init {
		DataBase.initialize(context)
	}

	private class BookFake : Book {
		override val bookName = ""
		override val cost = 0
		override var count = 0
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