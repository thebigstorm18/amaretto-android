package com.theostriches.amaretto.android.fragment;

import java.util.ArrayList;

import android.app.Activity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.theostriches.amaretto.android.MainActivity;
import com.theostriches.amaretto.android.model.Event;
import com.theostriches.amaretto.android.model.Point;

public class EventMapFragment extends SupportMapFragment {

	private MainActivity mMain;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mMain = (MainActivity) activity;
	}

	public void updateMap(Point p) {
		getMap().moveCamera(
				CameraUpdateFactory.newLatLngZoom(new LatLng(p.getLatitude(), p.getLongitude()), 14));
	}

	public void updateEvents() {
		GoogleMap map = getMap();
		for (Event e : mMain.getEventList()) {
			map.addMarker(new MarkerOptions()
					.position(new LatLng(e.getLatitude(), e.getLongitude())).title(e.getTitle())
					.snippet(e.getDescription().toString()));
		}
	}
	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// View v = inflater.inflate(R.layout.fragment_newevent, container, false);
	// return v;
	// }

	// @Override
	// public void onStart() {
	// super.onStart();
	// GoogleMap map = getMap();
	// }
}
