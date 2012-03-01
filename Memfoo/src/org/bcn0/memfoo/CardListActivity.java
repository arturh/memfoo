package org.bcn0.memfoo;

import java.io.IOException;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public final class CardListActivity extends ListActivity {
	ListView listView;
	MySQLiteOpenHelper db = new MySQLiteOpenHelper(this);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			db.createDataBase();
			db.openDataBase();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("MEMFOO", "db.createDatabase failed");
			finish();
		}
		Cursor c = db.getCardsCursor();
		startManagingCursor(c);
		ListAdapter adapter = new SimpleCursorAdapter(
				this, // context
				R.layout.cardlistrow,
				c,
				new String[] { db.KANJI, db.KANA, db.MEANING },
				new int[] { R.id.tvKanji, R.id.tvKana, R.id.tvMeaning }); // flags
		getListView().setAdapter(adapter);

	}

}
