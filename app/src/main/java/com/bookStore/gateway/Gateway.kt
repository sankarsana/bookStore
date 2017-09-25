package com.bookStore.gateway

import com.bookStore.model.Book

interface Gateway {

	var selectedBook: Book

	fun fetchBookList(searchText: String): List<Book>
}