package com.bookStore.model

import java.util.*

interface SaleEntry {
	val id: Int
	val date: Calendar
	val books: MutableList<Book>
	var sum: Int
	var note: String
	fun calculateSum(): Int
	fun addBook(book: Book)
}

