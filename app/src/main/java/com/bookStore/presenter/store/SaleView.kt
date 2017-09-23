package com.bookStore.presenter.store

import com.bookStore.model.BookForeSale

interface SaleView {

	fun startSelectBookActivity()

	fun updateBooks(books: List<BookForeSale>)
}
