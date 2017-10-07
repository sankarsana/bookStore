package com.bookStore.ui

import java.text.SimpleDateFormat
import java.util.*

const val NEW_BOOK = "newBook"

private val simpleDateFormat = SimpleDateFormat("d.MM.yyyy", Locale.getDefault())

fun Calendar.getDateSimple() = simpleDateFormat.format(this.time)!!