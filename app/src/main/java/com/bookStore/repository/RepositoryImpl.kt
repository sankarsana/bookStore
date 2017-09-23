package com.bookStore.repository

import android.content.Context
import com.bookStore.App.DataBase
import com.bookStore.presenter.Repository

class RepositoryImpl(context: Context) : Repository {

	init {
		DataBase.initialize(context)
	}
}