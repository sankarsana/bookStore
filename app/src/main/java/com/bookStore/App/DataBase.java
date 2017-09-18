package com.bookStore.App;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.Toast;

import com.bookStore.R;

import java.io.*;

import static android.widget.Toast.LENGTH_LONG;

public class DataBase extends SQLiteOpenHelper implements BaseColumns {

	public final static String DB_NAME = "bookStoreDB";
	public final static String DB_PATH = "//data//data//com.bookStore//databases//" + DB_NAME;
	private static SQLiteDatabase db;
	private static DataBase dataBase;

	private DataBase(Context context) {
		super(context, DB_NAME, null, 1);
	}

	public static void initialize(App context) {
		if (db != null) {
			Toast.makeText(context, "Повторная инициализация!", LENGTH_LONG).show();
			throw new ExceptionInInitializerError("Повторная инициализация!");
		}
		dataBase = new DataBase(context);
		db = dataBase.getWritableDatabase();
	}

	public static SQLiteDatabase get() {
		if (!db.isOpen())
			db = dataBase.getWritableDatabase();
		return db;
	}

	public static void closeDataBase() {
		db.close();
		dataBase.close();
	}

	public static void clearDataBase() {
		SQLiteDatabase db = get();
		db.delete("books", null, null);
		db.delete("ImportBooks", null, null);
		db.delete("ImportDate", null, null);
		db.delete("ImportDetail", null, null);
		db.delete("writers", null, null);
		db.delete("Persons", null, null);
		db.delete("InStock", null, null);
		db.delete("CardDate", null, null);
		db.delete("CardEntries", null, null);
		db.delete("Calc", null, null);
		initializeDefault(db);
	}

	private static void initializeDefault(SQLiteDatabase db) {
		rewriteTable(db, "books", R.raw.books);
		rewriteTable(db, "writers", R.raw.writers);
		db.execSQL("INSERT INTO Persons VALUES(1, 'Склад (продажа)', '100')");
		db.execSQL("INSERT INTO Persons VALUES(2, 'Магазин', '100')");
	}

	private static void rewriteTable(SQLiteDatabase db, String tableName, int rawResource) {
		db.beginTransaction();
		db.delete(tableName, null, null);
		InputStream inputStream = App.getContext().getResources().openRawResource(rawResource);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				if (!line.isEmpty() && !line.startsWith("//"))
					db.execSQL("INSERT INTO " + tableName + " VALUES (" + line + ")");
			}
			bufferedReader.close();
			db.setTransactionSuccessful();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		db.endTransaction();
	}

	public static void log_d(String text) {
		Log.d("my", text);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS books " +
				"(_id integer PRIMARY KEY AUTOINCREMENT , " +
				"bookName TEXT not null, " +
				"shortName TEXT, " +
				"writerId INTEGER default '1', " +
				"categoryId INTEGER default '0', " +
				"cost INTEGER DEFAULT '0', " +
				"count INTEGER DEFAULT '0', " +
				"sort TEXT)");

		db.execSQL("CREATE TABLE IF NOT EXISTS writers " +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"writer TEXT," +
				"sort integer);");


		db.execSQL("CREATE TABLE IF NOT EXISTS Categories " +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"category TEXT, " +
				"value REAL)");
		db.execSQL("INSERT INTO Categories VALUES(0, 'None', '0')");
		db.execSQL("INSERT INTO Categories VALUES(1, 'Mag', '0.1')");
		db.execSQL("INSERT INTO Categories VALUES(2, 'Small', '0.25')");
		db.execSQL("INSERT INTO Categories VALUES(3, 'Med', '0.5')");
		db.execSQL("INSERT INTO Categories VALUES(4, 'Big', '1')");
		db.execSQL("INSERT INTO Categories VALUES(5, 'MB', '2')");
		db.execSQL("INSERT INTO Categories VALUES(6, 'Deluxe', '6')");

		db.execSQL("CREATE TABLE IF NOT EXISTS ImportBooks " +
				"(imDate INTEGER NOT NULL, " +
				"bookId INTEGER NOT NULL, " +
				"count INTEGER NOT NULL);");

		db.execSQL("CREATE TABLE IF NOT EXISTS ImportDate " +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"dateFld integer)");

		db.execSQL("CREATE TABLE IF NOT EXISTS ImportDetail " +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"dateId INTEGER NOT NULL, " +
				"bookId INTEGER NOT NULL, " +
				"count INTEGER NOT NULL);");

		db.execSQL("CREATE TABLE IF NOT EXISTS Persons " +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
				"name TEXT NOT NULL, " +
				"percent INTEGER)");

		db.execSQL("CREATE TABLE IF NOT EXISTS InStock (" +
				"personId INTEGER NOT NULL, " +
				"bookId INTEGER NOT NULL, " +
				"count INTEGER NOT NULL);");

		db.execSQL("CREATE TABLE IF NOT EXISTS CardDate " +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
				"dateFld INTEGER, " +
				"personId INTEGER NOT NULL, " +
				"sum INTEGER DEFAULT '0' , " +
				"percent INTEGER DEFAULT '100' , " +
				"mark TEXT);");

		db.execSQL("CREATE TABLE IF NOT EXISTS CardEntries " +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
				"dateId INTEGER NOT NULL, " +
				"bookId INTEGER NOT NULL, " +
				"get INTEGER, " +
				"distr INTEGER, " +
				"ret INTEGER);");

		db.execSQL("CREATE TABLE IF NOT EXISTS Calc " +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
				"bookId INTEGER, " +
				"progress TEXT, " +
				"previousProgress TEXT, " +
				"count INTEGER DEFAULT '0', " +
				"previousCount INTEGER);");
		initializeDefault(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
