package com.theostriches.amaretto.android.model;

import java.util.Date;


public class Event {

	private String title;
	private String description;
	private User receiver;
	private User giver;
	private EventState state;
	private long timestampCreation;	
	private long timestampLimit;
	private Position position;

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

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public enum EventState {
		CREATED, ACCEPTED, FINISHED
	}
	
	public class Position {
		private int latitude;
		private int longitude;
		
		public Position() {			
		}

		public int getLatitude() {
			return latitude;
		}

		public void setLatitude(int latitude) {
			this.latitude = latitude;
		}

		public int getLongitude() {
			return longitude;
		}

		public void setLongitude(int longitude) {
			this.longitude = longitude;
		}
	}
}
