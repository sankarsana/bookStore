package com.bookStore.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.bookStore.R
import com.bookStore.model.SaleEntry

class SaleHistoryAdapter(private val context: Context) : BaseAdapter() {

	var entries = listOf<SaleEntry>()

	fun update(entries: List<SaleEntry>) {
		this.entries = entries
		notifyDataSetChanged()
	}

	override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
		var view = convertView
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_sale_history, parent, false)
		}
		val entry = getItem(position)
		(view?.findViewById(R.id.date) as TextView).text = entry.date.getDateSimple()
		(view.findViewById(R.id.sum) as TextView).text = entry.sum.toString()
		return view
	}

	override fun getItem(position: Int) = entries[position]

	override fun getItemId(position: Int) = entries[position].id.toLong()

	override fun getCount() = entries.size
}