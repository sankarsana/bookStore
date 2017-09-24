package com.bookStore.model

import java.util.*

class SaleEntryImpl : SaleEntry {

	override val customer = CustomerFake()

	override val books: MutableList<Book> = mutableListOf()

	override val date = Calendar.getInstance()

	override val note = ""

	override fun addBook(book: Book) {
		books.add(book)
	}

	override val sum: Int
		get() = books.sumBy { it.quantity * it.cost }
}
