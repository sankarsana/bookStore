package com.bookStore.presenter

import com.bookStore.model.SaleEntry

interface SaleHistoryView {

	fun update(sales: List<SaleEntry>)
}