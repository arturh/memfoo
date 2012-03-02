package org.bcn0.memfoo;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;

public class CardGraphActivity extends Activity {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	MySQLiteOpenHelper msoh;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cardgraph);
		CardGraph cg = (CardGraph)findViewById(R.id.cardGraph1);
		msoh = new MySQLiteOpenHelper(this);
		try {
			msoh.createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
			finish();
		}
		msoh.openDataBase();
		cg.setData(msoh.correctTally());
		msoh.close();
	}

	/**
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		// TODO Put your code here
	}
}
