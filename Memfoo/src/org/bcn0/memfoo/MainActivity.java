package org.bcn0.memfoo;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	SQLiteDatabase db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		if (!getSharedPreferences("memfoo.sharedpreferences", 0)
				.getBoolean("POPULATED_DATABASE", false)) {
			startActivity(new Intent(this, PopulateDatabaseActivity.class));
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
	
	public void startSpeechActivity(View v) {
		startActivity(new Intent(this, SpeechActivity.class));
	}
	
	public void testSound(View v) {
		Uri uri = Uri.parse("android.resource://org.bcn0.memfoo/raw/apa_to");
		MediaPlayer mp = MediaPlayer.create(this, uri);
		mp.start();
	}
}