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
		view.updateList(gateway.fetchBookList(""))
	}

	fun onSearchTextChange(text: String) {
		searchText = text
		val books = gateway.fetchBookList(searchText.toLowerCase())
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