package com.bookStore.presenter

import com.bookStore.App.App

object SelectBookPresenter {

	private lateinit var view: SelectBookView
	private val gateway = App.gateway
	private var searchText = ""

	fun bind(view: SelectBookView) {
		this.view = view
		view.updateList(gateway.fetchBookList(""))
	}

	fun onSearchTextChange(text: String) {
		searchText = text
		val books = gateway.fetchBookList(searchText.toLowerCase())
		view.updateList(books)
	}
}