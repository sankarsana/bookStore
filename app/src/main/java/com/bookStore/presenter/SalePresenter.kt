package com.bookStore.presenter

import com.bookStore.App.App
import com.bookStore.model.SaleEntry

object SalePresenter {

	private val gateway = App.Gateway
	private lateinit var view: SaleView
	private var entry = SaleEntry()

	fun onCreateView(saleView: SaleView) {
		view = saleView
		if (entry.books.isNotEmpty()) {
			view.updateBooks(entry.books)
			view.updateSum(entry.calculateSum())
		}
		view.updateDate(entry.date)
	}

	fun onButtonAddClick() {
		view.startSelectBookActivity()
	}

	fun onSelectBookActivityResult() {
		val book = SelectBookPresenter.selectedBook
		if (book != null) {
			entry.addBook(book)
			view.updateDate(entry.date)
			view.updateSum(entry.calculateSum())
			view.updateBooks(entry.books)
		}
	}

	fun onViewStop() {
		if (entry.books.isNotEmpty()) gateway.saveSale(entry)
		entry = SaleEntry()
	}
}
