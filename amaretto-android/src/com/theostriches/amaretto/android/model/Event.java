package com.theostriches.amaretto.android.model;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class Event implements Serializable {

	private static final long serialVersionUID = -4152020055833897028L;
	@SerializedName("title")
	private String title;
	@SerializedName("description")
	private String description;
	@SerializedName("receiver")
	private User receiver;
	@SerializedName("giver")
	private User giver;
	@SerializedName("state")
	private EventState state;
	@SerializedName("created_at")
	private long timestampCreation;
	@SerializedName("used_at")
	private long timestampUser;
	@SerializedName("until")
	private long timestampLimit;
	@SerializedName("latitude")
	private double latitude;
	@SerializedName("longitude")
	private double longitude;

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

	public Event() {
		Date date = new Date();
		timestampCreation = date.getTime();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public User getGiver() {
		return giver;
	}

	public void setGiver(User giver) {
		this.giver = giver;
	}

	public long getTimestampCreation() {
		return timestampCreation;
	}

	public void setTimestampCreation(long timestampCreation) {
		this.timestampCreation = timestampCreation;
	}

	public long getTimestampUser() {
		return timestampUser;
	}

	public void setTimestampUser(long timestampUser) {
		this.timestampUser = timestampUser;
	}

	public long getTimestampLimit() {
		return timestampLimit;
	}

	public void setTimestampLimit(long timestampLimit) {
		this.timestampLimit = timestampLimit;
	}

	public EventState getState() {
		return state;
	}

	public void setState(EventState state) {
		this.state = state;
	}

	public enum EventState {
		CREATED, ACCEPTED, FINISHED
	}

}
