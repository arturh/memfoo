package org.bcn0.memfoo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.bcn0.memfoo.DaoMaster.DevOpenHelper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class PopulateDatabaseActivity extends Activity {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	
	PopulateDatabaseTask pdt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.populatedatabase);


	}

	/**
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();

	}
	

	@Override
	protected void onResume() {
		super.onResume();
		pdt = new PopulateDatabaseTask();
		pdt.execute(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		pdt.cancel(true);
	}

	protected void done() {
		SharedPreferences settings = getSharedPreferences(
				MainActivity.SHARED_PREFERENCES_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(MainActivity.POPULATED_DATABASE, true);
		editor.commit();

		setResult(RESULT_OK);
		finish();
	}

}

class PopulateDatabaseTask extends
		AsyncTask<PopulateDatabaseActivity, Void, Void> {
	PopulateDatabaseActivity context;

	@Override
	protected Void doInBackground(PopulateDatabaseActivity... context) {
		try {
			populateDatabase(context[0]);
			Log.i("MEMFOO", "populateDatabase done.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		context.done();
	}

	private void populateDatabase(PopulateDatabaseActivity context_p) throws IOException {
		context = context_p;
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(
				context, "memfoo-db", null);

		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			DaoMaster daoMaster = new DaoMaster(db);
			DaoSession daoSession = daoMaster.newSession();
			
			CardDao cardDao = daoSession.getCardDao();
			
			db.delete(cardDao.getTablename(), null, null);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(
				context.getAssets().open("jlptn5.tsv")));
			
			String line;
			while ((line = in.readLine()) != null) {
				if (line.charAt(0) == '#') continue;
				if (line.charAt(0) == ' ') continue;
				Card newCard = null;
				try {
					String[] parts = line.split("\t");
					String id = parts[0];
					String kanji = parts[1];
					String kana = parts[2];
					String grammar = parts[3];
					String meaning = parts[4];
					String lesson = parts[5];
					String audio = parts[6];
					
					int lesson_index = Card.lessons.indexOf(lesson);
					if (lesson_index < 0) {
						Log.e("MEMFOO", "Lesson not found: " + lesson);
						lesson_index = 9999;
					}
					
					newCard = new Card(
							null, kanji, kana, meaning, audio, null, null, 0, lesson_index);
						
				} catch (Exception e) {
					Log.e("MEMFOO", "line not parsed:" + line);
				}
				cardDao.insert(newCard);
			}
		} finally {
			db.close();
		}
		
	}
}
