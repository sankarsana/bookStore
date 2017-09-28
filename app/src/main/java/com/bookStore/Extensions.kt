package com.bookStore

import java.text.SimpleDateFormat
import java.util.*

private val dbFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

fun Calendar.getDatabaseDate(): Int = dbFormat.format(this.time).toInt()