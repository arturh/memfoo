package org.bcn0.memfoo;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CardTestActivity extends Activity implements OnCompletionListener {
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 9998;
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
			mp.setOnCompletionListener(this);
		} catch (Exception e) {
			Log.i("MemFoo", e.toString());
		}
	}
	
	/**
	 * Fire an intent to start the speech recognition activity.
	 */
	public void startVoiceRecognitionActivity() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

		// Specify the calling package to identify your application
		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
				.getPackage().getName());

		// Display an hint to the user about what he should say.
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				"Speech recognition demo");

		// Given an hint to the recognizer about what the user is going to say
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

		// Specify how many results you want to receive. The results will be
		// sorted
		// where the first result is the one with higher confidence.
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);

		// Specify the recognition language. This parameter has to be specified
		// only if the
		// recognition has to be done in a specific language and not the default
		// one (i.e., the
		// system locale). Most of the applications do not have to set this
		// parameter.
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ja-JP");

		startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}

	/**
	 * Handle the results from the recognition activity.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			// Fill the list view with the strings the recognizer thought it
			// could have heard
			ArrayList<String> matches =
					data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			matches.get(0);
			if ((matches.indexOf(current.kana) != -1) ||
				(matches.indexOf(current.kana) != -1)) {
				Toast.makeText(this, "Correct!", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Incorrect! " + matches.get(0), Toast.LENGTH_LONG).show();
			}
			Toast.makeText(this, matches.toString(), Toast.LENGTH_LONG).show();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		startVoiceRecognitionActivity();
		
	}
}
