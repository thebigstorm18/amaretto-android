package com.theostriches.amaretto.android.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class User implements Serializable{

	private static final long serialVersionUID = 615521796464152996L;
	
	@SerializedName("username")
	private String name;
	@SerializedName("password")
	private String passwordHash;

	public User() {

	}

	public User(String name, String passwordHash) {
		this.name = name;
		this.passwordHash = passwordHash;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

}
