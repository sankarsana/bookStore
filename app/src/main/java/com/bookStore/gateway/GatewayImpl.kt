package com.bookStore.gateway

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.bookStore.App.DataBase
import com.bookStore.getDatabaseDate
import com.bookStore.model.Book
import com.bookStore.model.BookImpl
import com.bookStore.model.SaleEntry
import java.util.*

class GatewayImpl(context: Context) : Gateway {

	private val db: SQLiteDatabase
	private var selectedBook: Book? = null

	init {
		DataBase.initialize(context)
		db = DataBase.get()
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
			book.id = cursor.getInt(0)
			book.bookName = cursor.getString(1)
			book.count = cursor.getInt(3)
			book.cost = cursor.getInt(4)
			books.add(book)
		}
		cursor.close()
		return books
	}

	override fun saveSale(entry: SaleEntry) {
		val values = ContentValues(3)
		values.put("dateF", entry.date.getDatabaseDate())
		values.put("sum", entry.calculateSum())
		values.put("note", entry.note)
		db.beginTransaction()
		try {
			val storeSaleId = db.insert("StoreSale", null, values)
			for (book in entry.books) {
				values.clear()
				values.put("storeSaleId", storeSaleId)
				values.put("bookId", book.id)
				values.put("count", book.count)
				db.insert("StoreSaleBook", null, values)
			}
			db.setTransactionSuccessful()
		} finally {
			db.endTransaction()
		}
	}
}