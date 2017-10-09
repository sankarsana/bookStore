package com.bookStore.presenter

import com.bookStore.App.App
import com.bookStore.model.SaleEntry
import com.bookStore.model.SaleEntryImpl

object SalePresenter {

	private val gateway = App.Gateway
	private lateinit var view: SaleView
	var entry: SaleEntry = SaleEntryImpl()

	fun bind(saleView: SaleView) {
		view = saleView
		if (entry.books.isNotEmpty()) {
			view.updateBooks(entry.books)
			view.updateSum(entry.sum)
		}
		view.updateDate(entry.date)
	}

	fun onButtonAddClick() {
		view.startSelectBookActivity()
	}

	fun onSelectBookActivityResult() {
		val book = SelectBookPresenter.selectedBook ?: return
		entry.addBook(book)
		view.updateDate(entry.date)
		view.updateSum(entry.sum)
		view.updateBooks(entry.books)
	}

	fun onViewStop() {
		if (entry.books.isNotEmpty()) gateway.saveSale(entry)
		entry = SaleEntryImpl()
	}
}
