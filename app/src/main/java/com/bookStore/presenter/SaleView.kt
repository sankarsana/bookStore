package com.bookStore.presenter

import com.bookStore.model.Book
import java.util.*

interface SaleView {

	fun startSelectBookActivity()

	fun updateBooks(books: List<Book>)

	fun updateSum(sum: Int)

	fun updateDate(date: Calendar)
}
