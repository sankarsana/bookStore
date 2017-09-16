package com.bookStore.App;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.Toast;

public class DataBase extends SQLiteOpenHelper implements BaseColumns {

	public final static String DB_NAME = "bookStoreDB";
	public final static String DB_PATH = "//data//data//com.bookStore//databases//" + DB_NAME;
	private static SQLiteDatabase db;
	private static DataBase dataBase;
	private static Context context;

	private DataBase(Context context) {
		super(context, DB_NAME, null, 96);
	}

	public static void initialize(App context) {
		if (db != null) {
			Toast.makeText(context, "Повторная инициализация!", Toast.LENGTH_LONG).show();
			throw new ExceptionInInitializerError("Повторная инициализация!");
		}
		DataBase.context = context;
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
		db.delete("Books", null, null);
		db.delete("ImportBooks", null, null);
		db.delete("ImportDate", null, null);
		db.delete("ImportDetail", null, null);
		db.delete("Writers", null, null);
		db.delete("Persons", null, null);
		db.delete("InStock", null, null);
		db.delete("CardDate", null, null);
		db.delete("CardEntries", null, null);
		db.delete("Calc", null, null);
		initializeDefault(db);
	}

	private static void initializeDefault(SQLiteDatabase db) {
//        db.execSQL("INSERT INTO Writers VALUES(0, 'None')");
		db.execSQL("INSERT INTO Writers VALUES(1, 'Шрила Прабхупада')");
		db.execSQL("INSERT INTO Persons VALUES(1, 'Склад (продажа)', '100')");
		db.execSQL("INSERT INTO Persons VALUES(2, 'Магазин', '100')");
	}

	public static void log_d(String text) {
		Log.d("my", text);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS Books " +
				"(_id integer PRIMARY KEY AUTOINCREMENT , " +
				"bookName TEXT not null, " +
				"shortName TEXT, " +
				"writerId INTEGER default '1', " +
				"categoryId INTEGER default '0', " +
				"cost INTEGER DEFAULT '0', " +
				"count INTEGER DEFAULT '0', " +
				"search TEXT)"/* +
				"FOREIGN KEY (writerId) " +
                "REFERENCES Writers( _id), " +
                "FOREIGN KEY (categoryId) " +
                "REFERENCES Categories(_id));"*/);

		db.execSQL("CREATE TABLE IF NOT EXISTS Writers " +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
				"writer TEXT);");


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
				"count INTEGER NOT NULL);" /*+
					"FOREIGN KEY (bookId) " +
				    "REFERENCES Books( _id))"*/);

		db.execSQL("CREATE TABLE IF NOT EXISTS ImportDate " +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"dateFld integer)");

		db.execSQL("CREATE TABLE IF NOT EXISTS ImportDetail " +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"dateId INTEGER NOT NULL, " +
				"bookId INTEGER NOT NULL, " +
				"count INTEGER NOT NULL);" /*+
                    "FOREIGN KEY (" + dateId + ") " +
				    "REFERENCES " + ImportDate + "( _id), " +
					"FOREIGN KEY (" + bookId + ") " +
				    "REFERENCES " + Books + "( _id));"*/);

		db.execSQL("CREATE TABLE IF NOT EXISTS Persons " +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
				"name TEXT NOT NULL, " +
				"percent INTEGER)");

		db.execSQL("CREATE TABLE IF NOT EXISTS InStock (" +
				"personId INTEGER NOT NULL, " +
				"bookId INTEGER NOT NULL, " +
				"count INTEGER NOT NULL);" /*+
                    "FOREIGN KEY (" + personId + ") " +
				    "REFERENCES " + Persons + "( _id), " +
					"FOREIGN KEY (" + bookId + ") " +
				    "REFERENCES " + Books + "( _id));"*/);

		db.execSQL("CREATE TABLE IF NOT EXISTS CardDate " +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
				"dateFld INTEGER, " +
				"personId INTEGER NOT NULL, " +
				"sum INTEGER DEFAULT '0' , " +
				"percent INTEGER DEFAULT '100' , " +
				"mark TEXT);" /*+
                    "FOREIGN KEY (" + personId + ") " +
				    "REFERENCES " + Persons + "( _id));"*/);

		db.execSQL("CREATE TABLE IF NOT EXISTS CardEntries " +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
				"dateId INTEGER NOT NULL, " +
				"bookId INTEGER NOT NULL, " +
				"get INTEGER, " +
				"distr INTEGER, " +
				"ret INTEGER);" /*+
                    "FOREIGN KEY (" + dateId + ") " +
				    "REFERENCES " + CardDate + "( _id), " +
					"FOREIGN KEY (" + bookId + ") " +
				    "REFERENCES " + Books + "( _id));"*/);

		db.execSQL("CREATE TABLE IF NOT EXISTS Calc " +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
				"bookId INTEGER, " +
				"progress TEXT, " +
				"previousProgress TEXT, " +
				"count INTEGER DEFAULT '0', " +
				"previousCount INTEGER); " /*+
                         "FOREIGN KEY (" + bookId + ") " +
                         "REFERENCES " + Books + "( _id));"*/);
		initializeDefault(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS Books");
//        db.execSQL("DROP TABLE IF EXISTS ImportBooks");
//        db.execSQL("DROP TABLE IF EXISTS ImportDate");
//        db.execSQL("DROP TABLE IF EXISTS ImportDetail");
//        db.execSQL("DROP TABLE IF EXISTS Writers");
//        db.execSQL("DROP TABLE IF EXISTS Categories");
//        db.execSQL("DROP TABLE IF EXISTS Persons");
//        db.execSQL("DROP TABLE IF EXISTS InStock");
//        db.execSQL("DROP TABLE IF EXISTS CardDate");
//        db.execSQL("DROP TABLE IF EXISTS _CardEntriesPerson");
//        db.execSQL("DROP TABLE IF EXISTS Calc");
//        onCreate(db);

		db.execSQL("ALTER TABLE Books ADD COLUMN search TEXT");
		Cursor cursor = db.rawQuery("SELECT * FROM Books", null);
		if (cursor.moveToFirst()) {
			String string;
			db.beginTransaction();
			do {
				string = " " + cursor.getString(1).toLowerCase() + " " +
						cursor.getString(2).toLowerCase();
				db.execSQL(
						"UPDATE Books SET search = '" + string +
								"' WHERE _id = " + cursor.getLong(0));
			} while (cursor.moveToNext());
			db.setTransactionSuccessful();
			db.endTransaction();
			cursor.close();
		}
	}
}
