package com.bookStore.presenter

import com.bookStore.App.App
import com.bookStore.model.Book

object SelectBookPresenter {

	private lateinit var view: SelectBookView
	private val gateway = App.Gateway
	private var searchText = ""
	var selectedBook: Book? = null
		private set

	fun bind(view: SelectBookView) {
		this.view = view
		selectedBook = null
		val books = gateway.fetchBookList("")
		view.updateList(books)
		view.expandSearchItem()
	}

	fun onSearchTextChange(text: String) {
		searchText = text.toLowerCase()
		val books = gateway.fetchBookList(searchText)
		view.updateList(books)
	}

	fun onBookItemClick(book: Book) {
		selectedBook = book
		view.showCountDialog(book)
	}

	fun onCountDialogResult(bookCount: Int) {
		if (bookCount > 0) {
			selectedBook?.count = bookCount
			view.finish()
		} else {
			selectedBook = null
		}
	}
}