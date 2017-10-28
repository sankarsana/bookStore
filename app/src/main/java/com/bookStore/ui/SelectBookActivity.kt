package com.bookStore.ui

import android.os.Bundle
import android.view.Menu
import android.widget.AdapterView
import com.bookStore.R
import com.bookStore.model.Book
import com.bookStore.presenter.SelectBookPresenter
import com.bookStore.presenter.SelectBookView
import kotlinx.android.synthetic.main.activity_select_book.*

class SelectBookActivity : BaseActivity(), SelectBookView, InputCountDialog.InputCountDialogListener {

	private lateinit var adapter: SelectBookAdapter
	private val presenter = SelectBookPresenter
	private val dialog = InputCountDialog()

	public override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_select_book)
		adapter = SelectBookAdapter()
		listView.adapter = adapter
		presenter.bind(this)
		listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
			presenter.onBookItemClick(adapter.getItem(position))
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.activity_select_book, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun updateList(books: List<Book>) = adapter.update(books)

	override fun expandSearchItem() {
		isExpandSearchItem = true
	}

	override fun onSearchTextChange(text: String) = presenter.onSearchTextChange(text)

	override fun showCountDialog(book: Book) = dialog.show(supportFragmentManager, "")

	override fun onDialogResult(bookCount: Int) = presenter.onCountDialogResult(bookCount)

	override fun onSaveInstanceState(outState: Bundle?) {}
}
