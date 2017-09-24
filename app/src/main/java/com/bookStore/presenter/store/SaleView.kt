package com.bookStore.presenter.store

import com.bookStore.model.Book

interface SaleView {

	fun startSelectBookActivity()

	fun updateBooks(books: List<Book>)

	fun updateSum(sum: Int)
}
