package com.bookStore.ui

import android.os.Bundle
import com.bookStore.R
import com.bookStore.model.SaleEntry
import com.bookStore.presenter.SaleHistoryPresenter
import com.bookStore.presenter.SaleHistoryView
import kotlinx.android.synthetic.main.activity_sale_history.*

class SaleHistoryActivity : BaseActivity(), SaleHistoryView {

	private val presenter = SaleHistoryPresenter
	private val adapter = SaleHistoryAdapter(this)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_sale_history)
		listView.adapter = adapter
		presenter.bind(this)
		listView.setOnItemClickListener { _, _, position, _ -> presenter.onItemClick(position) }
	}

	override fun update(sales: List<SaleEntry>) = adapter.update(sales)
}
