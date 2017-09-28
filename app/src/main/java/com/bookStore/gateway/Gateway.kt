package com.bookStore.gateway

import com.bookStore.model.Book
import com.bookStore.model.SaleEntry

interface Gateway {

	fun fetchBookList(searchText: String): List<Book>

	fun saveSale(entry: SaleEntry)
}