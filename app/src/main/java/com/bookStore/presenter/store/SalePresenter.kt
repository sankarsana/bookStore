package com.bookStore.presenter.store

import com.bookStore.App.App
import com.bookStore.model.SaleEntry
import com.bookStore.model.SaleEntryImpl

object SalePresenter {

	private val repository = App.repository
	private lateinit var view: SaleView
	private lateinit var saleEntry: SaleEntry

	fun bind(saleView: SaleView) {
		view = saleView
		saleEntry = SaleEntryImpl()
	}

	fun onButtonAddClick() {
		view.startSelectBookActivity()
	}

	fun onSelectBookActivityResult() {
		saleEntry.addBook(repository.selectedBook)
		view.updateBooks(saleEntry.books)
		view.updateSum(saleEntry.sum)
	}
}
