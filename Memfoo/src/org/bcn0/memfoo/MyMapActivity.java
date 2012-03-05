package org.bcn0.memfoo;

import android.os.Bundle;

import com.google.android.maps.MapActivity;

public class MyMapActivity extends MapActivity {
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
