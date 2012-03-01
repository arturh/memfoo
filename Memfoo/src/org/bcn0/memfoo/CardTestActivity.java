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
	private TextView tvKanji;
	private Button btnKana;
	private TextView tvMeaning;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cardtest);

		showAnswerLayout = (LinearLayout) findViewById(R.id.showAnswerLayout);
		answerButtonsLayout = (LinearLayout) findViewById(R.id.answerButtonsLayout);
		
		tvKanji = (TextView) findViewById(R.id.tvKanji);
		btnKana = (Button) findViewById(R.id.btnKana);
		tvMeaning = (TextView) findViewById(R.id.tvMeaning);

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
		btnKana.setVisibility(View.VISIBLE);
		tvMeaning.setVisibility(View.VISIBLE);
		
		showAnswerLayout.setVisibility(View.GONE);
		answerButtonsLayout.setVisibility(View.VISIBLE);
	}

	public void showFront(View v) {
		btnKana.setVisibility(View.GONE);
		tvMeaning.setVisibility(View.GONE);
		
		showAnswerLayout.setVisibility(View.VISIBLE);
		answerButtonsLayout.setVisibility(View.GONE);
	}

	public void rememberCard(View v) {
		db.rememberedCard(current);
		
//		try {
//		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//		long[] pattern = {0, 50, 50};
//		vibrator.vibrate(pattern, current.correct + 1);
//		
//		} catch (Exception e) {
//			Log.i("MEMFOO", e.toString());
//		}
		
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
		if (current.kanji.equals("")) {
			tvKanji.setText(current.kana);
			btnKana.setText("\u25B7 " + current.kana);
			tvMeaning.setText(current.meaning);
		} else {
			tvKanji.setText(current.kanji);
			btnKana.setText("\u25B7 " + current.kana);
			tvMeaning.setText(current.meaning);
		}

		showFront(null);

	}

	public void playKana(View v) {
		try {
			Uri uri = Uri.parse("android.resource://org.bcn0.memfoo/raw/"
					+ this.current.audio);
			MediaPlayer mp = MediaPlayer.create(this, uri);
			mp.start();
		} catch (Exception e) {
			Log.i("MemFoo", e.toString());
		}
	}
}
