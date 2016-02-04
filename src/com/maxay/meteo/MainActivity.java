package com.maxay.meteo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.maxay.meteo.map.CustomMarker;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	// Google Map
	private GoogleMap googleMap;
	private HashMap<CustomMarker, Marker> markersHashMap;
	private Iterator<Entry<CustomMarker, Marker>> iter;
	private CameraUpdate cu;
	private CustomMarker customMarkerOne, customMarkerTwo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_map);

		try {
			// Loading map
			initilizeMap();
			initializeUiSettings();
			initializeMapLocationSettings();
			initializeMapType();
			initializeMapViewSettings();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// initilizeMap();
	}

	private void initilizeMap() {

		googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment)).getMap();

		// check if map is created successfully or not
		if (googleMap == null) {
			Toast.makeText(getApplicationContext(), getString(R.string.error_create_map), Toast.LENGTH_SHORT).show();
		}

		(findViewById(R.id.mapFragment)).getViewTreeObserver().addOnGlobalLayoutListener(
				new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						(findViewById(R.id.mapFragment)).getViewTreeObserver().removeOnGlobalLayoutListener(this);
						setCustomMarkerOnePosition();
						setCustomMarkerTwoPosition();
					}
				});
	}

	void setCustomMarkerOnePosition() {
		customMarkerOne = new CustomMarker("markerOne", 40.7102747, -73.9945297);
		addMarker(customMarkerOne);
	}

	void setCustomMarkerTwoPosition() {
		customMarkerTwo = new CustomMarker("markerTwo", 43.7297251, -74.0675716);
		addMarker(customMarkerTwo);
	}

	public void zoomToMarkers(View v) {
		zoomAnimateLevelToFitMarkers(120);
	}

	public void initializeUiSettings() {
		googleMap.getUiSettings().setCompassEnabled(true);
		googleMap.getUiSettings().setRotateGesturesEnabled(false);
		googleMap.getUiSettings().setTiltGesturesEnabled(true);
		googleMap.getUiSettings().setZoomControlsEnabled(true);
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		
		
	}

	public void initializeMapLocationSettings() {
		googleMap.setMyLocationEnabled(true);
	}

	public void initializeMapType() {
		googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
	}

	public void initializeMapViewSettings() {
		googleMap.setIndoorEnabled(true);
		googleMap.setBuildingsEnabled(false);
	}

	//this is method to help us set up a Marker that stores the Markers we want to plot on the map
	public void setUpMarkersHashMap() {
		if (markersHashMap == null) {
			markersHashMap = new HashMap<CustomMarker, Marker>();
		}
	}

	//this is method to help us add a Marker into the hashmap that stores the Markers
	public void addMarkerToHashMap(CustomMarker customMarker, Marker marker) {
		setUpMarkersHashMap();
		markersHashMap.put(customMarker, marker);
	}

	//this is method to help us find a Marker that is stored into the hashmap
	public Marker findMarker(CustomMarker customMarker) {
		iter = markersHashMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<CustomMarker, Marker> mEntry = iter.next();
			CustomMarker key = (CustomMarker) mEntry.getKey();
			if (customMarker.getCustomMarkerId().equals(key.getCustomMarkerId())) {
				Marker value = (Marker) mEntry.getValue();
				return value;
			}
		}
		return null;
	}


	//this is method to help us add a Marker to the map
	public void addMarker(CustomMarker customMarker) {
		MarkerOptions markerOption = new MarkerOptions().position(
				new LatLng(customMarker.getCustomMarkerLatitude(), customMarker.getCustomMarkerLongitude())).icon(
						BitmapDescriptorFactory.defaultMarker());

		Marker newMark = googleMap.addMarker(markerOption);
		addMarkerToHashMap(customMarker, newMark);
	}

	//this is method to help us remove a Marker
	public void removeMarker(CustomMarker customMarker) {
		if (markersHashMap != null) {
			if (findMarker(customMarker) != null) {
				findMarker(customMarker).remove();
				markersHashMap.remove(customMarker);
			}
		}
	}

	//this is method to help us fit the Markers into specific bounds for camera position
	public void zoomAnimateLevelToFitMarkers(int padding) {
		LatLngBounds.Builder b = new LatLngBounds.Builder();
		iter = markersHashMap.entrySet().iterator();

		while (iter.hasNext()) {
			Entry<CustomMarker, Marker> mEntry = iter.next();
			CustomMarker key = (CustomMarker) mEntry.getKey();
			LatLng ll = new LatLng(key.getCustomMarkerLatitude(), key.getCustomMarkerLongitude());
			b.include(ll);
		}
		LatLngBounds bounds = b.build();

		// Change the padding as per needed
		cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
		googleMap.animateCamera(cu);
	}

	//this is method to help us move a Marker.
	public void moveMarker(CustomMarker customMarker, LatLng latlng) {
		if (findMarker(customMarker) != null) {
			findMarker(customMarker).setPosition(latlng);
			customMarker.setCustomMarkerLatitude(latlng.latitude);
			customMarker.setCustomMarkerLongitude(latlng.longitude);
		}
	}
}