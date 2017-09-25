package com.bookStore.App

import android.content.Context
import android.support.v7.view.CollapsibleActionView
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import com.bookStore.R

class MySearchView(context: Context) : LinearLayout(context, null), CollapsibleActionView {

	private val mCloseButton: ImageView
	private val mSearchHint: ImageView
	private val queryEditText: EditTextImmStateRemember
	private var dataOfSearch: IDataOfSearch? = null

	init {
		val inflater = LayoutInflater.from(context)
		inflater.inflate(R.layout.my_search_view, this, true)

		queryEditText = findViewById(R.id.search_src_text) as EditTextImmStateRemember
		mCloseButton = findViewById(R.id.search_close_btn) as ImageView
		mSearchHint = findViewById(R.id.search_hint) as ImageView

		mCloseButton.setOnClickListener { queryEditText.setText("") }

		queryEditText.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
			}

			override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
				updateView()
				dataOfSearch?.refresh(s.toString().toLowerCase())
			}

			override fun afterTextChanged(s: Editable) {
			}
		})
	}

	fun setDataOfSearch(dataOfSearch: IDataOfSearch) {
		this.dataOfSearch = dataOfSearch
	}

	override fun onActionViewExpanded() {
		updateView()
		queryEditText.requestFocus()
		App.setImeVisibility(context, true, queryEditText)
	}

	override fun onActionViewCollapsed() {
		updateView()
		App.setImeVisibility(context, false, queryEditText)
		clearFocus()
		queryEditText.clearFocus()
		queryEditText.setText("")
	}

	fun setHint(hint: CharSequence) {
		queryEditText.hint = hint
	}

	private fun updateView() {
		val hasText = !TextUtils.isEmpty(queryEditText.text)
		mCloseButton.visibility = if (hasText) VISIBLE else GONE
		mSearchHint.visibility = if (hasText) GONE else VISIBLE
	}

	class EditTextImmStateRemember(context: Context, attrs: AttributeSet? = null)
		: AppCompatEditText(context, attrs) {

		override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
			super.onWindowFocusChanged(hasWindowFocus)
			val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			if (hasWindowFocus)
				imm.showSoftInput(this, 0)
		}
	}
}

