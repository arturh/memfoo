package org.bcn0.memfoo;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CardTestActivity extends Activity {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */

	LinearLayout showAnswerLayout, answerButtonsLayout;
	WebView wvFront, wvBack;
	Button btnPlay;
	private MySQLiteOpenHelper db;
	private Card current;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cardtest);

		showAnswerLayout = (LinearLayout) findViewById(R.id.showAnswerLayout);
		answerButtonsLayout = (LinearLayout) findViewById(R.id.answerButtonsLayout);

		wvFront = (WebView) findViewById(R.id.wvFront);
		wvBack = (WebView) findViewById(R.id.wvBack);
		
		btnPlay = (Button)findViewById(R.id.btnPlay);

		db = new MySQLiteOpenHelper(this);
		try {
			db.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Error("Could not create db");
		}
		db.openDataBase();
		loadNext();
	}

	public void showBack(View v) {
		wvBack.setVisibility(View.VISIBLE);
		showAnswerLayout.setVisibility(View.GONE);
		answerButtonsLayout.setVisibility(View.VISIBLE);
		btnPlay.setVisibility(View.VISIBLE);
	}

	public void showFront(View v) {
		wvBack.setVisibility(View.GONE);
		showAnswerLayout.setVisibility(View.VISIBLE);
		answerButtonsLayout.setVisibility(View.GONE);
		btnPlay.setVisibility(View.GONE);
	}

	public void rememberCard(View v) {
		db.rememberedCard(current);
		loadNext();
	}

	public void forgetCard(View v) {
		db.forgotCard(current);
		
		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = {0, 100, 100, 100};
		vibrator.vibrate(pattern, -1);
		
		loadNext();
	}

	public void loadNext() {
		if (db.dueCardsCount() > 0) {
			this.current = db.nextDue();
		} else {
			this.current = db.nextNew();
		}

		String front, back;

		if (current.kanji != null && current.kanji.length() > 0) {
			front = "<center><font size=10>" + current.kanji;
			back =  "<font size=3>"  + current.kana + "<br/>" + current.meaning;
		} else {
			front = "<center><font size=10>" + current.kana;
			back = "<font size=3>" + current.meaning;
		}

		wvFront.loadDataWithBaseURL(null, front, "text/html", "UTF-8", null);
		wvBack.loadDataWithBaseURL(null, back, "text/html", "UTF-8", null);

		showFront(null);

	}

	public void playSound(View v) {
		try {
		Uri uri = Uri.parse("android.resource://org.bcn0.memfoo/raw/" + this.current.audio);
		MediaPlayer mp = MediaPlayer.create(this, uri);
		mp.start();
		} catch (Exception e){
			Log.i("MemFoo", e.toString());
		}
	}
}
