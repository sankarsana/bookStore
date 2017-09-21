package com.bookStore.ui

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

class SaleAdapter(private val books: List<BookForeSale>) : BaseAdapter() {

	override fun getCount(): Int = books.size

	override fun getItem(position: Int): BookForeSale = books[position]

	override fun getItemId(position: Int): Long = books[position].saleId.toLong()

	override fun getView(position: Int, convertView: View, parent: ViewGroup): View? {
		return null
	}
}
