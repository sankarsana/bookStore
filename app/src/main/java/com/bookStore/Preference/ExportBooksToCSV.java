package com.bookStore.Preference;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bookStore.App.DataBase;
import com.bookStore.Store.Categories;
import com.bookStore.Store.Writer;

import java.io.*;

public class ExportBooksToCSV extends FileHelper {


	public void export() {
		export(BOOKS_CSV);
	}

	public void autoExport() {
		export(BOOKS_AUTO_BACKUP_CSV);
	}

	private void export(String fileName) {
		if (!isSDCardMounted())
			return;
		File fileCSV = new File(getAppFolder(), fileName);

		if (fileCSV.exists())
			fileCSV.delete();

		SQLiteDatabase db = DataBase.get();
		Cursor cursorBooks = db.query("Books", null, null, null, null, null, "bookName");
		if (!cursorBooks.moveToFirst())
			return;
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileCSV));
			do {
				bufferedWriter.write(getBookString(cursorBooks));
				bufferedWriter.newLine();
			} while (cursorBooks.moveToNext());

			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		cursorBooks.close();
	}

	private String getBookString(Cursor cursor) {
		return
				cursor.getString(1) + SEMICOLON +
						cursor.getString(2) + SEMICOLON +
						Writer.getWriterName(cursor.getLong(3)) + SEMICOLON +
						Categories.getInstance().getCategory(cursor.getInt(4)).getName() + SEMICOLON +
						cursor.getInt(5) + SEMICOLON +
						cursor.getInt(6);
	}
}
