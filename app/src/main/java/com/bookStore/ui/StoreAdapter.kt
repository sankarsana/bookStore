package com.bookStore.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bookStore.App.CursorSearchAdapter
import com.bookStore.R

class StoreAdapter(private val context: Context) : CursorSearchAdapter() {

	private class ViewHolder {
		lateinit var name: TextView
		lateinit var cost: TextView
	}

	override fun getQuery() =
			"SELECT _id, bookName, shortName, cost, count FROM books ORDER BY sort"

	override fun getQuery(search: String): String {
		return "SELECT _id, bookName, shortName, cost, count" +
				" FROM books" +
				" WHERE sort LIKE '" + search.toLowerCase() + "%'" +
				" or sort LIKE '% " + search.toLowerCase() + "%'" +
				" ORDER BY  sort"
	}

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		var view = convertView

		cursor.moveToPosition(position)
		val holder: ViewHolder
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_store, parent, false) as View
			holder = ViewHolder()
			holder.name = view.findViewById(R.id.bookName) as TextView
			holder.cost = view.findViewById(R.id.cost) as TextView
			view.tag = holder
		} else
			holder = view.tag as ViewHolder

		holder.name.text = cursor.getString(1)
		holder.cost.text = cursor.getString(3) + "р."
		return view
	}
}