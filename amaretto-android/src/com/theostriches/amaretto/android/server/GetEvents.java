package com.theostriches.amaretto.android.server;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.theostriches.amaretto.android.model.Event;
import com.theostriches.amaretto.android.util.Constant;
import com.theostriches.amaretto.android.util.Log;

/**
 * @author Antonio Prada <toniprada@gmail.com>
 * 
 * 
 */
public class GetEvents extends Thread {

	public static final int CODE_OK = 43;
	public static final int CODE_ERROR = 41;

	private Handler mHandler;

	private boolean done = false;

	public GetEvents(Handler handler) {
		this.mHandler = handler;

	}

	@Override
	public void run() {
		try {
			String response = HttpRequest.get(Constant.SERVER_URL + "/api/event").body();
			Log.w("GETEVENTS:" + response);
			Gson gson = new Gson();
			Type collectionType = new TypeToken<ArrayList<Event>>() {
			}.getType();
			ArrayList<Event> events = gson.fromJson(response, collectionType);
			mHandler.sendMessage(Message.obtain(mHandler, CODE_OK, events));
		} catch (Exception e) {
			Log.e("other exception, maybe encrypt: " + e.getMessage());
			sendMessage(CODE_ERROR);
		}
	}

	private void sendMessage(int code) {
		if (!done) {
			done = true;
			mHandler.sendEmptyMessage(code);
		}
	}

}
