package com.bookStore.model

import com.bookStore.gateway.NO_ID
import java.util.*

class SaleEntryImpl(override var id: Int = NO_ID) : SaleEntry {

	override var date: Calendar = Calendar.getInstance()
	override val books: MutableList<Book> = mutableListOf()
	override var sum = 0
	override var note = ""

	override fun addBook(book: Book) {
		books.add(book)
		sum += book.count * book.cost
	}
}