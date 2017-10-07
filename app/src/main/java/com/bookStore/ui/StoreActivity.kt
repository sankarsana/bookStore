package com.bookStore.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import com.bookStore.Calculation.CalcActivity
import com.bookStore.Persons.PersonsActivity
import com.bookStore.Preference.PrefActivity
import com.bookStore.R
import com.bookStore.Reports.JointActivity
import com.bookStore.store.BookDetailDialog
import kotlinx.android.synthetic.main.activity_store.*

class StoreActivity : BaseActivity() {

	private lateinit var bookDetail: BookDetailDialog
	private val adapter = StoreAdapter(this)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_store)
		bookDetail = BookDetailDialog(this)
		listView.adapter = adapter
		listView.setOnItemClickListener { _, _, _, id -> bookDetail.show(id) }
		lInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
		isExpandSearchItem = true
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.activity_store, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onSearchTextChange(text: String) {
		adapter.refresh(text)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		val id = item.itemId
		when (id) {
			R.id.menu_sale -> startActivity(Intent(this, SaleActivity::class.java))
			R.id.menu_sale_history -> startActivity(Intent(this, SaleHistoryActivity::class.java))
			R.id.menu_store_persons -> startActivity(Intent(this, PersonsActivity::class.java))
			R.id.store_menu_calculate -> startActivity(Intent(this, CalcActivity::class.java))
			R.id.store_menu_reports -> startActivity(Intent(this, JointActivity::class.java))
			R.id.store_menu_preference -> startActivity(Intent(this, PrefActivity::class.java))
		}
		return super.onOptionsItemSelected(item)
	}

	companion object {
		var lInflater: LayoutInflater? = null
			private set
	}
}
