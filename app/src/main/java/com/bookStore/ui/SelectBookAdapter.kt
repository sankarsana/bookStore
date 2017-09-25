package com.bookStore.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.bookStore.R
import com.bookStore.model.Book

class SelectBookAdapter() : BaseAdapter() {

	private var books = listOf<Book>()

	fun update(books: List<Book>) {
		this.books = books
		notifyDataSetChanged()
	}

	override fun getItemId(position: Int) = position.toLong()

	override fun getCount() = books.size

	override fun getItem(position: Int) = books[position]

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		var view = convertView
		if (convertView == null) {
			view = LayoutInflater.from(parent.context).inflate(R.layout.item_select_book, parent, false)
		}
		val book = books[position]
		(view?.findViewById(R.id.selectBook_name) as TextView).text = book.bookName
		(view.findViewById(R.id.selectBook_count) as TextView).text = book.quantity.toString()
		return view
	}
}