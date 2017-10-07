package com.bookStore.gateway

import java.text.SimpleDateFormat
import java.util.*

private val dbFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

fun Calendar.getDatabaseDate(): Int = dbFormat.format(this.time).toInt()

fun toCalendar(dateDB: String): Calendar {
	val calendar = Calendar.getInstance()
	calendar.time = dbFormat.parse(dateDB)
	return calendar
}