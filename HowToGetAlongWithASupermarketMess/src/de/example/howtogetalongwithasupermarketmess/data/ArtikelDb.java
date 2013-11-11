package de.example.howtogetalongwithasupermarketmess.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ArtikelDb extends SQLiteOpenHelper {

	private static final String TAG = ArtikelDb.class.getSimpleName();

	private static final String DATENBANK_NAME = "artikel.db";
	private static final int DATENBANK_VERSION = 1;
	private static final String TABLE_NAME = "artikel";
	public static final String ID = "_id";
	public static final String NAME = "name";
	public static final String COST = "cost";
	public static final String QUANTITY = "quantity";

	private static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + "("
			+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + NAME
			+ " TEXT NOT NULL," + COST + " TEXT NOT NULL," + QUANTITY
			+ " TEXT NOT NULL" + ");";

	public ArtikelDb(Context context) {
		super(context, DATENBANK_NAME, null, DATENBANK_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	public void insert() {
		SQLiteDatabase db = getWritableDatabase();
		long rowId = -1;
		try {
			ContentValues values = new ContentValues();

			values.put(NAME, "Apfel");
			values.put(COST, "5.50");
			values.put(QUANTITY, "50");

			rowId = db.insert(TABLE_NAME, null, values);
			
			values.put(NAME, "Gans");
			values.put(COST, "67.50");
			values.put(QUANTITY, "15");
			
			rowId = db.insert(TABLE_NAME, null, values);

			values.put(NAME, "Oliven");
			values.put(COST, "2.45");
			values.put(QUANTITY, "10");
			
			rowId = db.insert(TABLE_NAME, null, values);

			/*values.put(NAME, "Zitronen");
			values.put(COST, "3.50");
			values.put(QUANTITY, "5");*/

			//rowId = db.insert(TABLE_NAME, null, values);
		} catch (SQLException e) {
			Log.e("DB", e.toString());
		} finally {
			Log.i("DB", "insert:" + rowId);
		}

	}

	public void delete(long id) {
		// ggf. Datenbank öffnen
		SQLiteDatabase db = getWritableDatabase();
		int numDeleted = db.delete(TABLE_NAME, ID + " = ?",
				new String[] { Long.toString(id) });
		Log.d(TAG, "delete(): id=" + id + " -> " + numDeleted);
	}

	public void deleteAll() {
		// ggf. Datenbank öffnen
		SQLiteDatabase db = getWritableDatabase();
		int numDeleted = db.delete(TABLE_NAME, ID + " != ?",
				new String[] { "-1" });
		Log.d(TAG, "delete(): alle " + numDeleted);
	}

	public Cursor select(String artikel) {
		SQLiteDatabase db = getWritableDatabase();

		if (artikel != "") {
			return db.query(TABLE_NAME, new String[] { "_id", "name", "cost",
					"quantity" }, "name = ?", new String[] { artikel }, null,
					null, null, null);
		} else {
			return db.query(TABLE_NAME, null, null, null, null, null, null,
					null);
		}
	}
}
