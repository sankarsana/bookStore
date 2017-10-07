package com.bookStore.model

import com.bookStore.gateway.NO_ID
import java.util.*

class SaleEntryImpl(override val id: Int = NO_ID) : SaleEntry {

	override var date: Calendar = Calendar.getInstance()
	override val books: MutableList<Book> = mutableListOf()
	override var sum = 0
	override var note = ""

	override fun calculateSum() = books.sumBy { it.count * it.cost }

	override fun addBook(book: Book) {
		books.add(book)
	}
}