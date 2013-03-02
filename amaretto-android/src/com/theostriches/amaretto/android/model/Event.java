package com.theostriches.amaretto.android.model;

import java.util.Date;

public class Event {

	private String title;
	private String description;
	private User receiver;
	private User giver;
	private Date limitDate;
	private EventState state;

	public Event() {
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

	public Date getLimitDate() {
		return limitDate;
	}

	public void setLimitDate(Date limitDate) {
		this.limitDate = limitDate;
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
