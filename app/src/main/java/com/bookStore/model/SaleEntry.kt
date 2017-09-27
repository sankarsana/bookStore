package com.bookStore.model

class SaleEntry {

	val books: MutableList<Book> = mutableListOf()

	fun calculateSum() = books.sumBy { it.count * it.cost }

	fun addBook(book: Book) {
		books.add(book)
	}
}
