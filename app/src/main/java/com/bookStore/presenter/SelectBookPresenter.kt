package com.bookStore.presenter

import com.bookStore.App.App
import com.bookStore.model.Book

object SelectBookPresenter {

	private lateinit var view: SelectBookView
	private val gateway = App.Gateway
	private var searchText = ""
	lateinit var selectedBook: Book

	fun bind(view: SelectBookView) {
		this.view = view
		val books = gateway.fetchBookList("")
		view.updateList(books)
	}

	fun onSearchTextChange(text: String) {
		searchText = text.toLowerCase()
		val books = gateway.fetchBookList(searchText)
		view.updateList(books)
	}

	fun onBookItemClick(book: Book) {
		selectedBook = book
		view.showInputCountDialog(book)
	}

	fun onCountResult(bookCount: Int) {
		selectedBook.count = bookCount
		gateway.selectedBook = selectedBook
		view.finish()
	}
}