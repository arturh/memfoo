package org.bcn0.memfoo;

import org.bcn0.memfoo.DaoMaster.DevOpenHelper;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public final class CardListActivity extends ListActivity {
	static final String LESSON_ID = "LESSON_ID";
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
		

		
		Bundle extras = getIntent().getExtras();
		long lesson_id = extras.getLong(LESSON_ID, -1);
		
		final String table = CardDao.TABLENAME;
		final String[] columns = cardDao.getAllColumns();
		String selection = null;
		String[] selectionArgs = null;
		final String groupBy = null;
		final String having = null;
		final String orderBy = CardDao.Properties.Lesson.columnName + " ASC,"
				             + CardDao.Properties.Id.columnName + " ASC";
				
		if (lesson_id > 0) {
			selection = "lesson = ?";
			selectionArgs = new String[] { Long.toString(lesson_id)}; 
		}

		Cursor cursor = db.query(
			table, columns, selection, selectionArgs, groupBy, having, orderBy);
		
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
