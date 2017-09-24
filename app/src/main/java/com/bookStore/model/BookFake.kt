package com.bookStore.model

class BookFake(override var quantity: Int, bookName: String) : BookImpl() {

	init {
		this.bookName = bookName
	}
}