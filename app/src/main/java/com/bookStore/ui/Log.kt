package com.bookStore.ui

import com.bookStore.BuildConfig.DEBUG

object Log {

	fun show(clazz: Any, msg: String) {
		if (DEBUG) android.util.Log.i("my", "${clazz.javaClass.simpleName} : $msg")
	}
}