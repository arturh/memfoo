package org.bcn0.memfoo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.bcn0.memfoo.DaoMaster.DevOpenHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
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

	LinearLayout answerButtonsLayout;
	WebView wvFront, wvBack;
	Button btnPlay;

	private TextView tvStatus;
	private TextView tvKanji;
	private Button btnKana;
	private TextView tvMeaning;
	private Button btnShowAnswer;

	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	private CardDao cardDao;

	private Card currentCard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cardtest);

		answerButtonsLayout = (LinearLayout) findViewById(R.id.linearLayout1);
		btnShowAnswer = (Button) findViewById(R.id.btnShowBack);
		
		tvStatus = (TextView) findViewById(R.id.tvStatus);

		tvKanji = (TextView) findViewById(R.id.tvKanji);
		btnKana = (Button) findViewById(R.id.btnKana);
		tvMeaning = (TextView) findViewById(R.id.tvMeaning);

		DevOpenHelper helper =
			new DaoMaster.DevOpenHelper(this, "memfoo-db", null);

		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		
		cardDao = daoSession.getCardDao();
		
		btnKana.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View arg0) {
				startVoiceRecognitionActivity();
				return true;
			}
			
		});
		loadNext();
	}



	public void showBack(View v) {
		btnKana.setVisibility(View.VISIBLE);
		tvMeaning.setVisibility(View.VISIBLE);

		btnShowAnswer.setVisibility(View.GONE);
		answerButtonsLayout.setVisibility(View.VISIBLE);
	}

	public void showFront(View v) {
		btnKana.setVisibility(View.GONE);
		tvMeaning.setVisibility(View.GONE);
		btnShowAnswer.setVisibility(View.VISIBLE);

		answerButtonsLayout.setVisibility(View.GONE);
	}

	public void rememberCard(View v) {
		// How long to wait after <correct> consecutive correct answers
		// (seconds)
		int[] correctToDue =
			{ 90, 600, 6000, 6000, 6000, 6000, 6000, 6000, 6000 };
		currentCard.setCorrect(currentCard.getCorrect() + 1);
		
		if (currentCard.getIntroduced() == null) {
			currentCard.setIntroduced(new Date());
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.SECOND, correctToDue[currentCard.getCorrect()]);
		currentCard.setDue(calendar.getTime());

		cardDao.update(currentCard);

		loadNext();
	}

	public void forgetCard(View v) {
		currentCard.setCorrect(0);

		if (currentCard.getIntroduced() == null) {
			currentCard.setIntroduced(new Date());
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.SECOND, 90);
		currentCard.setDue(calendar.getTime());

		cardDao.update(currentCard);

		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { 0, 100, 100, 100 };
		vibrator.vibrate(pattern, -1);

		loadNext();
	}



	public void loadNext() {
		if (Card.dueCards(cardDao).size() > 0) {
			this.currentCard = Card.dueCards(cardDao).get(0);
		} else {
			this.currentCard = Card.newCards(cardDao).get(0);
		}

		if (currentCard.getKanji().equals("")) {
			tvKanji.setText(currentCard.getKana());
			btnKana.setText("\u25B7 " + currentCard.getKana());
			tvMeaning.setText(currentCard.getMeaning());
		} else {
			tvKanji.setText(currentCard.getKanji());
			btnKana.setText("\u25B7 " + currentCard.getKana());
			tvMeaning.setText(currentCard.getMeaning());
		}

		showFront(null);
		updateStatus();
	}

	public void playKana(View v) {
		try {
			AssetFileDescriptor afd = getAssets().openFd(
					currentCard.getAudio() + ".mp3");
			MediaPlayer mp = new MediaPlayer();
			mp.setDataSource(
				afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			mp.prepare();
			mp.start();
			mp.setOnCompletionListener(this);
		} 
		catch (IllegalStateException e) {
		    Log.d("MEMFOO", "IllegalStateException: " + e.getMessage());
		}
		catch (IOException e) {
		    Log.d("MEMFOO", "IOException: " + e.getMessage());
		}
		catch (IllegalArgumentException e) {
		    Log.d("MEMFOO", "IllegalArgumentException: " + e.getMessage());
		}
		catch (SecurityException e) {
		    Log.d("MEMFOO", "SecurityException: " + e.getMessage());
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
		// sorted where the first result is the one with higher confidence.
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);

		// Specify the recognition language. This parameter has to be specified
		// only if the recognition has to be done in a specific language and
		// not the default one (i.e., the system locale).
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
			ArrayList<String> matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			matches.get(0);
			if ((matches.indexOf(currentCard.getKanji()) != -1)
					|| (matches.indexOf(currentCard.getKana()) != -1)) {
				Toast.makeText(this, "Correct!", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Incorrect! " + matches.get(0),
						Toast.LENGTH_LONG).show();
			}
			Log.i("MEMFOO", "matches: " + matches.toString());
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
	}
	
	public void updateStatus() {
		tvStatus.setText(
				"due: " + Integer.toString(Card.dueCards(cardDao).size()) +	"    " +
				"new: " + Integer.toString(Card.introducedToday(cardDao))
				);
	}
}
