package org.bcn0.memfoo;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LessonListActivity extends ListActivity {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ArrayAdapter<String> adapter =
			new ArrayAdapter<String>(
				this, R.layout.lessonrow, R.id.textView1,
				(String[]) Card.lessons.toArray());
		
		getListView().setAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, CardListActivity.class);
		intent.putExtra(CardListActivity.LESSON_ID, id);
		startActivity(intent);
	}
}
