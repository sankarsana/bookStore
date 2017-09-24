package com.bookStore.repository

import android.content.Context
import com.bookStore.App.DataBase
import com.bookStore.model.Book

class RepositoryImpl(context: Context) : Repository {

	override var selectedBook: Book = BookFake()

	init {
		DataBase.initialize(context)
	}

	private class BookFake : Book {
		override val bookName = ""
		override val cost = 0
		override val quantity = 0
	}
}