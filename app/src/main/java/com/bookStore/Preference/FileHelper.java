package com.bookStore.Preference;

import android.os.Environment;

import com.bookStore.App.DataBase;
import com.bookStore.App.Utilities;

import java.io.File;

public abstract class FileHelper {

	protected final String BOOKS_CSV = "books.csv";
	protected final String BOOKS_AUTO_BACKUP_CSV = "books_autoBackup.csv";
	protected final String SEMICOLON = ";";
	protected final String QUOTE = String.valueOf('"');

	public static File getAppFolder() {
		File folder = new File(Environment.getExternalStorageDirectory(), "BookStore");
		if (!folder.exists())
			folder.mkdir();
		return folder;
	}

	public static File getDataBaseBackupFolder() {
		File folder = new File(getAppFolder(), "DataBaseBackups");
		if (!folder.exists())
			folder.mkdir();
		return folder;
	}

	public static String getDataBaseBackupName() {
		return Utilities.getCurrentDate_simple_string() + "_" + DataBase.DB_NAME + ".db";
	}

	boolean isSDCardMounted() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
}
