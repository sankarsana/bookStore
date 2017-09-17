package com.bookStore.Preference;

import android.os.Environment;

import com.bookStore.App.DataBase;
import com.bookStore.App.Utilities;
import com.bookStore.store.Book;
import com.bookStore.store.Categories;
import com.bookStore.store.Store;

import java.io.*;

public class ImportBooksFromCSV extends FileHelper {

	public ImportBooksFromCSV(boolean clearData) {

		if (!isSDCardMounted())
			return;

		File fileCsv = new File(Environment.getExternalStorageDirectory(), BOOKS_CSV);
		if (clearData)
			DataBase.get().delete("books", null, null);
		DataBase.get().beginTransaction();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileCsv));
			String line;

			while ((line = br.readLine()) != null) {
				Book book = getBookFromLine(line);
				book.insertToBase();
				if (book.count != 0)
					Store.setBookCount(book.id, book.count);
			}
			DataBase.get().setTransactionSuccessful();
			DataBase.get().endTransaction();
			br.close();
		} catch (IOException e) {
			DataBase.get().endTransaction();
		}
	}

	private Book getBookFromLine(String line) {
		int index = 0;
		String[] values = new String[6];
		line = line.trim();

		do {
			if (line.startsWith(QUOTE)) { // Если вначале кавычка
				int pos = line.indexOf(QUOTE, 1);
				if (pos == -1) {
					values[index] = line;
					break;
				} else {
					values[index] = line.substring(1, pos - 1);
					line = line.substring(pos + 1);
				}
			} else {
				int pos = line.indexOf(";");
				if (pos == -1) {
					values[index] = line;
					break;
				} else {
					values[index] = line.substring(0, pos);
					line = line.substring(pos + 1);
				}
			}
			index++;
		} while (line.length() != 0 && index < values.length);

		if (!Utilities.isInteger(values[4]))
			values[4] = "0";
		if (values[1].length() == 0)
			values[1] = Book.createShortName(values[0]);
		Book book = new Book(values[0], values[1], values[4]);
		if (Utilities.isInteger(values[5]))
			book.count = Integer.parseInt(values[5]);
		book.category = Categories.getInstance().getCategoryOrNone(values[3]);
		return book;
	}
}
