package com.bookStore.presenter

import com.bookStore.App.App
import com.bookStore.model.SaleEntry

object SaleHistoryPresenter {

	private lateinit var view: SaleHistoryView
	private lateinit var sales: List<SaleEntry>
	var selectedEntry: SaleEntry? = null

	fun bind(view: SaleHistoryView) {
		this.view = view
		sales = App.Gateway.fetchSales()
		view.update(sales)
	}

	fun onItemClick(position: Int) {
		SalePresenter.entry = sales[position]
		view.startSaleActivity()
	}

}