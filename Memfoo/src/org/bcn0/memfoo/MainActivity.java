package org.bcn0.memfoo;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
	private static final int POPULATE_DATABASE_REQUEST_CODE = 477;
	static final String SHARED_PREFERENCES_NAME = "memfoo.sharedpreferences";
	static final String POPULATED_DATABASE = "POPULATED_DATABASE";
	/** Called when the activity is first created. */
	SQLiteDatabase db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		if (!getSharedPreferences(SHARED_PREFERENCES_NAME, 0)
				.getBoolean(POPULATED_DATABASE, false)) {
			startActivityForResult(
				new Intent(this, PopulateDatabaseActivity.class), POPULATE_DATABASE_REQUEST_CODE);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == POPULATE_DATABASE_REQUEST_CODE) {
			if (resultCode == RESULT_CANCELED) {
				finish();
			}
		}
	}

	public void startCardTestActivity(View v) {
		startActivity(new Intent(this, CardTestActivity.class));
	}
	
	public void startCardGraphActivity(View v) {
		startActivity(new Intent(this, CardGraphActivity.class));
	}
	
	public void startCardListActivity(View v) {
		startActivity(new Intent(this, CardListActivity.class));
	}
	
	public void startLessonListActivity(View v) {
		startActivity(new Intent(this, LessonListActivity.class));
	}
	
	public void startSpeechActivity(View v) {
		startActivity(new Intent(this, SpeechActivity.class));
	}
	
	public void testSound(View v) {
		Uri uri = Uri.parse("android.resource://org.bcn0.memfoo/raw/apa_to");
		MediaPlayer mp = MediaPlayer.create(this, uri);
		mp.start();
	}
}