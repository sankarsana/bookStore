package com.bookStore.model

import java.util.*

class SaleEntry {

	val date: Calendar = Calendar.getInstance()
	val books: MutableList<Book> = mutableListOf()
	var note = ""

	fun calculateSum() = books.sumBy { it.count * it.cost }

	fun addBook(book: Book) {
		books.add(book)
	}
}
