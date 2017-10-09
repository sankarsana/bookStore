package com.bookStore.model

import java.util.*

interface SaleEntry {
	var id: Int
	val date: Calendar
	val books: MutableList<Book>
	var sum: Int
	var note: String
	fun addBook(book: Book)
}

