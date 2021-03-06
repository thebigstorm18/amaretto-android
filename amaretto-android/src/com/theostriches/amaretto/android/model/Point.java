package com.theostriches.amaretto.android.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Point implements Serializable {

	private static final long serialVersionUID = -599138042090042775L;
	
	@SerializedName("latitude")
	private double latitude;
	@SerializedName("longitude")
	private double longitude;

	public Point(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Point() {
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}