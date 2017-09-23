package com.bookStore.model

import java.util.*

interface SaleEntry {

	val customer: Customer

	val books: List<BookForeSale>

	val date: Calendar

	val note: String

	fun addBook(bookId: Int)
}