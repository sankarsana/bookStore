package com.bookStore.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import com.bookStore.App.App
import com.bookStore.R
import com.bookStore.store.UpdateBookAct

class SelectBookActivity : BaseActivity(), AdapterView.OnItemClickListener {

	private lateinit var adapter: SelectBookAdapter

	public override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_select_book)
		adapter = SelectBookAdapter()
		val listView = findViewById(R.id.listView) as ListView
		listView.adapter = adapter
		listView.onItemClickListener = this
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.select_book, menu)
		if (intent.getBooleanExtra(NEW_BOOK, false)) {
			menu.getItem(1).isVisible = false
		}
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == R.id.selectBook_add) {
			startActivityForResult(Intent(this, UpdateBookAct::class.java), 1)
		}
		return super.onOptionsItemSelected(item)
	}

	override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
		intent.putExtra("bookId", id)
		intent.putExtra("bookName", adapter.getName(position))
		App.repository.selectedBook = adapter.getItem(position)
		setResult(RESULT_OK, intent)
		finish()
	}

	override fun onSearchTextChange(text: String) {

	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
		super.onActivityResult(requestCode, resultCode, data)
		if (resultCode == RESULT_OK) {
			setResult(RESULT_OK, data)
			finish()
		}
	}

	companion object {

		val NEW_BOOK = "newBook"
	}
}
