package com.bookStore.gateway

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.bookStore.App.DataBase
import com.bookStore.model.Book
import com.bookStore.model.BookImpl
import com.bookStore.model.SaleEntry
import com.bookStore.model.SaleEntryImpl
import java.util.*

class GatewayImpl(context: Context) : Gateway {

	private val db: SQLiteDatabase

	init {
		DataBase.initialize(context)
		db = DataBase.get()
	}

	override fun fetchBookList(searchText: String): List<Book> {
		val where = if (searchText.isEmpty()) "" else {
			"WHERE sort LIKE '$searchText%' OR sort LIKE '% $searchText%'"
		}
		val query = "SELECT _id, bookName, shortName, count, cost FROM books $where ORDER BY sort"
		val cursor = DataBase.get().rawQuery(query, null)
		val books = ArrayList<Book>()
		while (cursor.moveToNext()) {
			val book = BookImpl(cursor.getInt(0))
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
		values.put("sum", entry.sum)
		values.put("note", entry.note)
		db.beginTransaction()
		try {
			if (entry.id == NO_ID) {
				entry.id = db.insert("StoreSale", null, values).toInt()
			} else db.update("StoreSale", values, "_id = ${entry.id}", null)
			db.delete("StoreSaleBook", "storeSaleId = ${entry.id}", null)
			for (book in entry.books) {
				values.clear()
				values.put("storeSaleId", entry.id)
				values.put("bookId", book.id)
				values.put("count", book.count)
				db.insert("StoreSaleBook", null, values)
			}
			db.setTransactionSuccessful()
		} finally {
			db.endTransaction()
		}
	}

	override fun fetchSales(): List<SaleEntry> {
		val sales = mutableListOf<SaleEntryImpl>()
		val entriesCursor = db.rawQuery(
				"select _id, dateF, personId, sum, note from StoreSale order by dateF desc, _id", null)
		while (entriesCursor.moveToNext()) {
			val entry = SaleEntryImpl(entriesCursor.getInt(0))
			entry.date = toCalendar(entriesCursor.getString(1))
			entry.sum = entriesCursor.getInt(3)
			val booksCursor = db.rawQuery("select bookId, books.bookName, StoreSaleBook.count" +
					" from StoreSaleBook" +
					" inner join books on books._id = StoreSaleBook.bookId" +
					" where storeSaleId = ${entry.id}", null)
			while (booksCursor.moveToNext()) {
				val book = BookImpl(booksCursor.getInt(0))
				book.bookName = booksCursor.getString(1)
				book.count = booksCursor.getInt(2)
				entry.books.add(book)
			}
			booksCursor.close()
			sales.add(entry)
		}
		entriesCursor.close()
		return sales
	}
}