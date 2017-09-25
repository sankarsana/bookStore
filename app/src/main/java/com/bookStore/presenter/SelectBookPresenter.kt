package com.bookStore.presenter

object SelectBookPresenter {

	private lateinit var view: SelectBookView

	fun bind(view: SelectBookView) {
		this.view = view
	}
}