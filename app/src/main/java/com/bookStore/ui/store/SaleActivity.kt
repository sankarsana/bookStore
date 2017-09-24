package com.bookStore.ui.store

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.widget.TextView
import com.bookStore.App.BaseListActivity
import com.bookStore.R
import com.bookStore.model.Book
import com.bookStore.presenter.store.SalePresenter
import com.bookStore.presenter.store.SaleView
import com.bookStore.store.SelectBookActivity

class SaleActivity : BaseListActivity<SaleAdapter>(), SaleView {

	private val presenter = SalePresenter
	lateinit var sum: TextView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_store_sale)
		val fab = findViewById(R.id.add) as FloatingActionButton
		sum = findViewById(R.id.sum) as TextView
		fab.setOnClickListener { presenter.onButtonAddClick() }

		listAdapter = SaleAdapter(this)
		presenter.bind(this)
	}

	override fun updateBooks(books: List<Book>) {
		listAdapter.update(books)
	}

	override fun updateSum(sum: Int) {
		this.sum.text = getString(R.string.sum, sum)
	}

	override fun startSelectBookActivity() {
		val intent = Intent(this, SelectBookActivity().javaClass)
		startActivityForResult(intent, 0)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (data != null) {
			val bookName = data.getStringExtra("bookName")
			presenter.onSelectBookActivityResult(bookName)
		}
	}
}
