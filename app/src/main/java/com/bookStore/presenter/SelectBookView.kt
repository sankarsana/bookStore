package com.bookStore.presenter

import com.bookStore.model.Book

interface SelectBookView {

	fun updateList(books: List<Book>)
}