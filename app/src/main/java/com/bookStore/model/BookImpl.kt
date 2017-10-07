package com.bookStore.model

import com.bookStore.gateway.NO_ID

class BookImpl(
		override val id: Int = NO_ID) : Book {

	override var bookName = ""

	override var cost = 0;

	override var count = 0
}