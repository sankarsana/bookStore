package com.bookStore.ui.store

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.bookStore.R
import com.bookStore.model.BookForeSale

class SaleAdapter(private val context: Context) : BaseAdapter() {

	private var books: List<BookForeSale> = listOf()

	override fun getCount(): Int = books.size

	override fun getItem(position: Int): BookForeSale = books[position]

	override fun getItemId(position: Int): Long = position.toLong()

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
		var view = convertView
		if (convertView == null)
			view = LayoutInflater.from(context).inflate(R.layout.item_card_store, parent, false)
		val book = books[position]
		(view?.findViewById(R.id.shortName) as TextView).text = book.toString()
		(view.findViewById(R.id.cardDistr) as TextView).text =
				if (book.quantity == 0) "" else book.quantity.toString()
		return view
	}

	fun update(books: List<BookForeSale>) {
		this.books = books
		notifyDataSetChanged()
	}
}
