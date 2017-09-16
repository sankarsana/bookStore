package com.bookStore.App;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utilities {

	private static final SimpleDateFormat DATE_FORMAT_DB = new SimpleDateFormat(
			"yyyyMMdd", Locale.getDefault());
	private static final SimpleDateFormat DATE_FORMAT_SIMPLE = new SimpleDateFormat(
			"d.MM.yyyy", Locale.getDefault());
	private static final SimpleDateFormat DATE_FORMAT_EXTEND = new SimpleDateFormat(
			"d MMMM yyyy", Locale.getDefault());
	private static final SimpleDateFormat DATE_FORMAT_YEAR = new SimpleDateFormat(
			"yyyy", Locale.getDefault());
	private static final SimpleDateFormat DATE_FORMAT_MANTH = new SimpleDateFormat(
			"MM", Locale.getDefault());
	private static final SimpleDateFormat DATE_FORMAT_DAY = new SimpleDateFormat(
			"dd", Locale.getDefault());

	public static String convertDateDBToSimple(String dateDB) {

		try {
			return DATE_FORMAT_SIMPLE.format(DATE_FORMAT_DB.parse(dateDB));
		} catch (ParseException e) {
			throw new RuntimeException("Incorrect date format");
		}
	}

	public static String convertDateDBToExtend(String dateDB) {
		try {
			return DATE_FORMAT_EXTEND.format(DATE_FORMAT_DB.parse(dateDB));
		} catch (ParseException e) {
			throw new RuntimeException("Incorrect date format");
		}
	}

	public static long getCurrentDate_DB_long() {
		return Long.parseLong(DATE_FORMAT_DB.format(new Date()));
	}

	public static String getCurrentDate_DB_string() {
		return DATE_FORMAT_DB.format(new Date());
	}

	public static String getCurrentDate_simple_string() {
		return DATE_FORMAT_SIMPLE.format(new Date());
	}

	public static Calendar convertDBToDate(String dateDB) {
		Calendar.getInstance();
		Calendar calendar = DATE_FORMAT_DB.getCalendar();
		try {
			calendar.setTime(DATE_FORMAT_DB.parse(dateDB));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar;
	}

	/**
	 * Извлекает значение даты.
	 *
	 * @param year  Year
	 * @param month Month
	 * @param day   Day
	 * @return String
	 */
	public static String getDate_DB_string(int year, int month, int day) {
		String monthStr = month < 10 ? "0" + month : "" + month;
		String dayStr = day < 10 ? "0" + day : "" + day;
		return year + monthStr + dayStr;
	}

	public static Long getDate_DB_long(int year, int month, int day) {
		return Long.parseLong(getDate_DB_string(year, month, day));
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}

	public static int getYear(String dateDB) {
		return Integer.parseInt(dateDB.substring(0, 4));
	}

	public static int getMonth(String dateDB) {
		return Integer.parseInt(dateDB.substring(4, 6));
	}

	public static int getDay(String dateDB) {
		return Integer.parseInt(dateDB.substring(6));
	}

	public static String convertDateTo_DB_String(Date date) {
		return DATE_FORMAT_DB.format(date);
	}

	public static long convertDateTo_DB_long(Date date) {
		return Long.parseLong(DATE_FORMAT_DB.format(date));
	}
}
