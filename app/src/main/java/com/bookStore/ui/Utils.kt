package com.bookStore.ui

import java.text.SimpleDateFormat
import java.util.*

const val NEW_BOOK = "newBook"
const val NO_ID = -1

private val simpleDateFormat = SimpleDateFormat("d.MM.yyyy", Locale.getDefault())

fun Calendar.getDateSimple() = simpleDateFormat.format(this.time)