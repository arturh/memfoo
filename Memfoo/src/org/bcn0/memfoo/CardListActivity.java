package org.bcn0.memfoo;

import java.io.IOException;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public final class CardListActivity extends ListActivity implements  OnItemClickListener {
	ListView listView;
	MySQLiteOpenHelper db = new MySQLiteOpenHelper(this);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			db.createDataBase();
			db.openDataBase();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("MEMFOO", "db.createDatabase failed");
			finish();
		}
		Cursor c = db.getCardsCursor();
		startManagingCursor(c);
		ListAdapter adapter = new SimpleCursorAdapter(
				this, // context
				R.layout.cardlistrow,
				c,
				new String[] { db.KANJI, db.KANA, db.MEANING },
				new int[] { R.id.tvKanji, R.id.tvKana, R.id.tvMeaning }); // flags
		getListView().setAdapter(adapter);
		getListView().setOnItemClickListener(this);
	}
	
	class MyListAdapter extends SimpleCursorAdapter {
		private final Context context;

		public MyListAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);
			this.context = context;
			
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater mInflater =
				(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.cardlistrow, null);
			return null;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}



}
