package com.maxay.meteo;

import com.maxay.meteo.gps.Gps;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Gps gps;

	FrameLayout layout = null;
	SearchView locationSV = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gps = new Gps(this);

		layout = (FrameLayout) FrameLayout.inflate(this, R.layout.activity_main, null);
		locationSV = (SearchView) layout.findViewById(R.id.location);

		searchLocation();
	}

	private void searchLocation() {
		Toast.makeText(this, "Latitude " + gps.getLatitude() + " | Longitude " + gps.getLongitude(), Toast.LENGTH_SHORT).show();
	}

}
