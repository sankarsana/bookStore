package com.bookStore.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.bookStore.R
import com.bookStore.model.Book

class SaleAdapter(private val context: Context) : BaseAdapter() {

	private var books: List<Book> = listOf()

	override fun getCount(): Int = books.size

	override fun getItem(position: Int): Book = books[position]

	override fun getItemId(position: Int): Long = position.toLong()

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
		var view = convertView
		if (view == null)
			view = LayoutInflater.from(context).inflate(R.layout.item_sale, parent, false)
		val book = books[position]
		(view?.findViewById(R.id.bookName) as TextView).text = book.bookName
		(view.findViewById(R.id.count) as TextView).text = book.count.toString()
		return view
	}

	fun update(books: List<Book>) {
		this.books = books
		notifyDataSetChanged()
	}
}
