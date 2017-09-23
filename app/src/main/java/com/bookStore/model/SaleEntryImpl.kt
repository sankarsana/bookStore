package com.bookStore.model

import java.util.*

class SaleEntryImpl : SaleEntry {

	override val customer = CustomerFake()

	override val books: MutableList<BookForeSale> = mutableListOf()

	override val date = Calendar.getInstance()

	override val note = ""

	override fun addBook(bookId: Int) {
		books.add(BookForSaleFake(2))
	}

	override val sum: Int
		get() = books.size * 50
}
