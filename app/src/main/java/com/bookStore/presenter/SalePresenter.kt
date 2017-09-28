package com.bookStore.presenter

import com.bookStore.App.App
import com.bookStore.model.SaleEntry

object SalePresenter {

	private val gateway = App.Gateway
	private lateinit var view: SaleView
	private var entry = SaleEntry()

	fun onCreateView(saleView: SaleView) {
		view = saleView
		if (entry.books.isNotEmpty()) updateView()
	}

	fun onButtonAddClick() {
		view.startSelectBookActivity()
	}

	fun onSelectBookActivityResult() {
		val book = SelectBookPresenter.selectedBook
		if (book != null) {
			entry.addBook(book)
			updateView()
		}
	}

	private fun updateView() {
		view.updateBooks(entry.books)
		view.updateSum(entry.calculateSum())
	}

	fun onBackPressed() {
		gateway.saveSale(entry)
		entry = SaleEntry()
	}
}
