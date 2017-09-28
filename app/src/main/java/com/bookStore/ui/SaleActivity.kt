package com.bookStore.ui

import android.content.Intent
import android.os.Bundle
import com.bookStore.R
import com.bookStore.model.Book
import com.bookStore.presenter.SalePresenter
import com.bookStore.presenter.SaleView
import kotlinx.android.synthetic.main.activity_store_sale.*
import kotlinx.android.synthetic.main.content_sale.*
import kotlinx.android.synthetic.main.content_sale_header.*

class SaleActivity : BaseActivity(), SaleView {

	private val presenter = SalePresenter
	private val adapter = SaleAdapter(this)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_store_sale)
		listView.adapter = adapter
		fabAdd.setOnClickListener { presenter.onButtonAddClick() }
		presenter.onCreateView(this)
	}

	override fun updateBooks(books: List<Book>) = adapter.update(books)

	override fun updateSum(sum: Int) {
		this.sum.text = getString(R.string.sum, sum)
	}

	override fun startSelectBookActivity() =
			startActivityForResult(Intent(this, SelectBookActivity().javaClass), 0)

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		presenter.onSelectBookActivityResult()
	}

	override fun onStop() {
		super.onStop()
		presenter.onViewStop()
	}
}
