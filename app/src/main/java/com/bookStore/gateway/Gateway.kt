package com.bookStore.gateway

import com.bookStore.model.Book

interface Gateway {

	fun fetchBookList(searchText: String): List<Book>

	fun pushSelected(book: Book)

	fun popSelected(): Book?
}