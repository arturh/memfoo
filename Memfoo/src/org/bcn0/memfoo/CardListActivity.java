package org.bcn0.memfoo;

import org.bcn0.memfoo.DaoMaster.DevOpenHelper;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public final class CardListActivity extends ListActivity {
	ListView listView;
	SQLiteDatabase db;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DevOpenHelper helper = new DaoMaster.DevOpenHelper(
			this, "memfoo-db",null);
		db = helper.getReadableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);
		DaoSession daoSession = daoMaster.newSession();
		CardDao cardDao = daoSession.getCardDao();
		
		String orderBy = CardDao.Properties.Lesson.columnName + " ASC,"
					   + CardDao.Properties.Id.columnName + " ASC";
		Cursor cursor = db.query(CardDao.TABLENAME, cardDao.getAllColumns(), null, null, null, null, orderBy);
		
		startManagingCursor(cursor);
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(
			this, R.layout.cardlistrow, cursor,
			new String[] {CardDao.Properties.Kanji.columnName, CardDao.Properties.Kana.columnName, CardDao.Properties.Meaning.columnName},
			new int[] { R.id.tvKanji, R.id.tvKana, R.id.tvMeaning});
		
		getListView().setAdapter(adapter);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		db.close();
		finish();
	}


}
