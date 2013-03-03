package com.theostriches.amaretto.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.theostriches.amaretto.android.model.User;

public class LocalDataManager {

	private static final String LOGGED = "logged";
	private static final String USERNAME = "username";
	private static final String PASS_MD5 = "passMd5";
	private SharedPreferences sp;

	public LocalDataManager(Context context) {
		sp = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public void setUser(User login) {
		Editor e = sp.edit();
		e.putString(USERNAME, login.getName());
		e.putString(PASS_MD5, login.getPasswordHash());
		e.putBoolean(LOGGED, true);
		e.commit();
	}

	public User getLogin() {
		if (sp.getBoolean(LOGGED, false)) {
			return new User(sp.getString(USERNAME, ""), sp.getString(PASS_MD5, ""));
		} else {
			return null;
		}
	}

	public void clearLogin() {
		Editor e = sp.edit();
		e.remove(USERNAME);
		e.remove(PASS_MD5);
		e.putBoolean(LOGGED, false);
		e.commit();
	}

}
