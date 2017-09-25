package com.bookStore.ui

import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import com.bookStore.R

abstract class BaseActivity : AppCompatActivity() {

	override fun setContentView(@LayoutRes layoutResID: Int) {
		super.setContentView(layoutResID)
		val toolbar = findViewById(R.id.toolbar) as Toolbar?
		setSupportActionBar(toolbar)
		if (layoutResID != R.layout.activity_store && toolbar != null) {
			supportActionBar?.setDisplayHomeAsUpEnabled(true)
			supportActionBar?.setHomeButtonEnabled(true)
			toolbar.setNavigationOnClickListener { onBackPressed() }
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		val searchItem = menu.findItem(R.id.action_search)
		val searchView = searchItem?.actionView as SearchView? ?: return false
		searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
			override fun onQueryTextSubmit(query: String) = false
			override fun onQueryTextChange(newText: String): Boolean {
				onSearchTextChange(newText)
				return true
			}
		})
		searchView.queryHint = searchItem.title
		return true
	}

	open fun onSearchTextChange(text: String) {}

	protected fun setToolbarTitle(title: String) {
		supportActionBar?.title = title
	}

	protected fun setToolbarSubtitle(title: String) {
		supportActionBar?.subtitle = title
	}
}
