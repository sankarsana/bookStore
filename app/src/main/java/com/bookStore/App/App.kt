package com.bookStore.App

import android.app.Application
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.bookStore.repository.Repository
import com.bookStore.repository.RepositoryImpl

class App : Application() {

	companion object {

		lateinit var context: Context
			private set
		lateinit var repository: Repository
			private set

		fun setImeVisibility(context: Context, visible: Boolean, view: View) {
			val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			if (visible)
				imm.showSoftInput(view, 0)
			else
				imm.hideSoftInputFromWindow(view.windowToken, 0)
		}
	}

	override fun onCreate() {
		super.onCreate()
		context = this
		repository = RepositoryImpl(this)
//		DataBase.initialize(this)
	}
}