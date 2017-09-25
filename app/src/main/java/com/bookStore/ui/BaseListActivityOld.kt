package com.bookStore.ui

import android.support.v4.view.MenuItemCompat
import android.view.Menu
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.HeaderViewListAdapter
import android.widget.ListAdapter
import android.widget.ListView
import com.bookStore.App.CursorSearchAdapter
import com.bookStore.App.IDataOfSearch
import com.bookStore.App.MySearchView
import com.bookStore.R

abstract class BaseListActivityOld<T : ListAdapter> : BaseActivity() {

	private var listView: ListView? = null

	protected var listAdapter: T
		get() {
			val adapter = getListView().adapter as T
			return if (adapter is HeaderViewListAdapter) {
				(adapter as HeaderViewListAdapter).wrappedAdapter as T
			} else {
				adapter
			}
		}
		set(adapter) {
			getListView().adapter = adapter
		}

	override fun onStart() {
		super.onStart()
		val adapter = listAdapter
		if (adapter is CursorSearchAdapter)
			(adapter as CursorSearchAdapter).refresh()
	}

	protected fun getListView(): ListView {
		if (listView == null) {
			listView = findViewById(android.R.id.list) as ListView
			listView?.onItemClickListener = OnItemClickListener { _, view, i, l ->
				onListItemClick(getListView(), view, i, l)
			}
		}
		return listView as ListView
	}

	protected open fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {}

	override fun onCreateOptionsMenu(menu: Menu): Boolean = initializeMySearchView(menu)

	protected fun initializeMySearchView(menu: Menu): Boolean {
		val searchItem = menu.findItem(R.id.my_action_search) ?: return false
		val searchView = MenuItemCompat.getActionView(searchItem) as MySearchView
		searchView.setHint(searchItem.titleCondensed)
		searchView.setDataOfSearch(getListView().adapter as IDataOfSearch)
		return true
	}
}
