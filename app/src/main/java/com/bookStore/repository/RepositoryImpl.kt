package com.bookStore.repository

import android.content.Context
import com.bookStore.App.DataBase

class RepositoryImpl(context: Context) : Repository {

	init {
		DataBase.initialize(context)
	}
}