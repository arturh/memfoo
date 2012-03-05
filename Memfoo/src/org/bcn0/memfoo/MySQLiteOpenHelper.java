package org.bcn0.memfoo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public final class MySQLiteOpenHelper extends SQLiteOpenHelper {

	private static final String CARDS_TABLE = "cards";
	private static final String DB_PATH = "/data/data/org.bcn0.memfoo/databases/";
	private static final String DB_NAME = "jlpt4.sqlite";

	// Column names
	static final String ID = "_id";
	static final String KANJI = "kanji";
	static final String KANA = "kana";
	static final String MEANING = "meaning";
	static final String AUDIO = "audio";
	static final String DUE = "due";
	static final String INTRODUCED = "introduced";
	static final String CORRECT = "correct";

	private static final String[] CARDS_TABLE_COLUMNS = { ID, KANJI, KANA,
			MEANING, AUDIO, DUE, INTRODUCED, CORRECT };

	private Context context;
	private SQLiteDatabase db;

	public MySQLiteOpenHelper(Context context) {
		super(context, DB_NAME, null, 1);
		this.context = context;
	}

	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			// do nothing - database already exist
		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();

			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
		} catch (SQLiteException e) {
			// database does't exist yet.
		}

		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {
		Log.i("MemFoo", "copyDatabase");
		// Open your local db as the input stream
		InputStream myInput = context.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void openDataBase() throws SQLException {

		// Open the database
		String myPath = DB_PATH + DB_NAME;
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);

	}

	@Override
	public synchronized void close() {
		if (db != null)
			db.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	private Card cardFromCursor(Cursor c) {
		return new Card(c.getInt(0), c.getString(1), c.getString(2),
				c.getString(3), c.getString(4), c.getInt(5), c.getInt(6),
				c.getInt(7));
	}

	private Cursor dueCards() {
		return db.query(CARDS_TABLE, null, "due < ?",
				new String[] { Long.toString((new Date()).getTime()) }, null,
				null, null);
	}

	public int dueCardsCount() {
		Cursor cursor = dueCards();
		int count = cursor.getCount();
		cursor.close();
		return count;
	}

	public Card nextDue() {
		Cursor c = dueCards();
		c.moveToFirst();
		c.getColumnIndex(ID);
		Card next = cardFromCursor(c);
		c.close();
		return next;
	}

	public Card nextNew() {
		Cursor c = db.query(CARDS_TABLE, CARDS_TABLE_COLUMNS, "introduced = 0",
				null, null, null, ID);

		c.moveToFirst();
		Card next = cardFromCursor(c);
		c.close();

		ContentValues values = new ContentValues();
		values.put(INTRODUCED, (new Date()).getTime());
		db.update(CARDS_TABLE, values, "_id = " + Integer.toString(next._id),
				null);

		return next;
	}

	public void rememberedCard(Card c) {
		final int[] CORRECT_DUE = { 30, 60, 90, 150, 300, 12 * 60 * 60,
				24 * 60 * 60, 2 * 24 * 60 * 60, 3 * 24 * 60 * 60,
				5 * 24 * 60 * 60, 8 * 24 * 60 * 60, 14 * 24 * 60 * 60,
				21 * 24 * 60 * 60, 30 * 24 * 60 * 60, 45 * 24 * 60 * 60,
				60 * 24 * 60 * 60, 60 * 24 * 60 * 60, 60 * 24 * 60 * 60,
				60 * 24 * 60 * 60, 60 * 24 * 60 * 60, 60 * 24 * 60 * 60,
				60 * 24 * 60 * 60, 60 * 24 * 60 * 60, 60 * 24 * 60 * 60,
				60 * 24 * 60 * 60, 60 * 24 * 60 * 60, 60 * 24 * 60 * 60,
				60 * 24 * 60 * 60, 60 * 24 * 60 * 60, 60 * 24 * 60 * 60,
				60 * 24 * 60 * 60, 60 * 24 * 60 * 60, 60 * 24 * 60 * 60,
				60 * 24 * 60 * 60, 60 * 24 * 60 * 60, 60 * 24 * 60 * 60,
				60 * 24 * 60 * 60, 60 * 24 * 60 * 60, 60 * 24 * 60 * 60,
				60 * 24 * 60 * 60, 60 * 24 * 60 * 60, 60 * 24 * 60 * 60,
				60 * 24 * 60 * 60, 60 * 24 * 60 * 60, 60 * 24 * 60 * 60,
				60 * 24 * 60 * 60, 60 * 24 * 60 * 60 };

		ContentValues values = new ContentValues();
		values.put(CORRECT, c.correct + 1);
		// Cards show up often due to demostration purposes. Adjust.
		values.put(DUE, (new Date()).getTime() + CORRECT_DUE[c.correct] * 1000);
		db.update(CARDS_TABLE, values, "_id = " + Integer.toString(c._id), null);
	}

	public void forgotCard(Card c) {
		ContentValues values = new ContentValues();
		values.put(CORRECT, 0);
		values.put(DUE, (new Date()).getTime() + 30 * 1000);
		db.update(CARDS_TABLE, values, "_id = " + Integer.toString(c._id), null);
	}

	public Cursor getCardsCursor() {
		return db.query(CARDS_TABLE, CARDS_TABLE_COLUMNS, null, null, null,
				null, ID);
	}

	public int[] correctTally() {
		Cursor c = db.query(CARDS_TABLE, new String[] { CORRECT, "COUNT(*)" },
				INTRODUCED + " != ''", null, CORRECT, null, null);
		int[] result = new int[15];
		while (c.moveToNext()) {
			int correct = c.getInt(0);
			int count = c.getInt(1);
			result[correct] = count;
		}
		c.close();
		return result;
	}

}
