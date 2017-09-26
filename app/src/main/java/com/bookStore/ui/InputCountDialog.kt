package com.bookStore.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bookStore.App.MyDialogFragment
import com.bookStore.R
import com.bookStore.model.Book
import com.bookStore.presenter.SelectBookPresenter
import kotlinx.android.synthetic.main.dialog_input_editor.*

class InputCountDialog : MyDialogFragment(), View.OnClickListener {

	private var allAvailableCount: Int = 0
	private lateinit var book: Book

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedState: Bundle?): View =
			inflater.inflate(R.layout.dialog_input_editor, container)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		btnOk.setOnClickListener(this)
		btnCancel.setOnClickListener(this)
		inputKeyboard.setEditText(inputCount)
		btnAll.setOnClickListener(this)
		book = SelectBookPresenter.selectedBook
		bookName.text = book.bookName
	}

	override fun onStart() {
		super.onStart()

		val currentBookCount = activity.intent.getIntExtra("currentCount", 0)
		if (currentBookCount != 0) {
			inputCount.setText(currentBookCount.toString())
			inputCount.selectAll()
		} else
			inputCount.setText("")

		allAvailableCount = activity.intent.getIntExtra("availableCount", 0)
		if (allAvailableCount != 0)
			btnAll.text = resources.getString(R.string.allCount, allAvailableCount)
		else
			btnAll.text = resources.getString(R.string.all)
	}

	override fun onClick(v: View) {
		when (v.id) {

			R.id.btnOk -> {
				var bookCount = 0
				if (inputCount.text.isNotEmpty())
					bookCount = inputCount.text.toString().toInt() - activity.intent.getIntExtra("currentCount", 0)
				(activity as InputCountDialogListener).onDialogPositiveResult(bookCount)
				dismiss()
			}

			R.id.btnCancel -> dismiss()

			R.id.btnAll -> if (allAvailableCount != 0) inputCount.setText(allAvailableCount.toString())
		}
	}

	interface InputCountDialogListener {

		fun onDialogPositiveResult(bookCount: Int)
	}
}
