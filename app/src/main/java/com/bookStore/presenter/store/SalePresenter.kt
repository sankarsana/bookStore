package com.bookStore.presenter.store

import com.bookStore.App.App
import com.bookStore.model.SaleEntry
import com.bookStore.model.SaleEntryImpl

object SalePresenter {

	private val repository = App.Gateway
	private lateinit var view: SaleView
	private lateinit var saleEntry: SaleEntry
	private var haveEntry = false

	fun onCreateView(saleView: SaleView) {
		view = saleView
		if (haveEntry) {
			updateView()
		} else {
			saleEntry = SaleEntryImpl()
			haveEntry = true
		}
	}

	fun onButtonAddClick() {
		view.startSelectBookActivity()
	}

	fun onSelectBookActivityResult() {
		saleEntry.addBook(repository.selectedBook)
		updateView()
	}

	private fun updateView() {
		view.updateBooks(saleEntry.books)
		view.updateSum(saleEntry.sum)
	}

	fun onBackPressed() {
		haveEntry = false
	}
}
