package com.theostriches.amaretto.android.server;

import java.util.HashMap;
import java.util.Map;

import android.os.Handler;

import com.theostriches.amaretto.android.model.Event;
import com.theostriches.amaretto.android.util.Constant;
import com.theostriches.amaretto.android.util.Log;

/**
 * @author Antonio Prada <toniprada@gmail.com>
 * 
 * 
 */
public class NewEvent extends Thread {

	public static final int CODE_OK = 43;
	public static final int CODE_ERROR = 41;

	private Handler mHandler;
	private Event event;

	private boolean done = false;

	public NewEvent(Handler handler, Event event) {
		this.mHandler = handler;
		this.event = event;
	}

	@Override
	public void run() {
		try {
			Map<String, String> data = new HashMap<String, String>();
			data.put("title", event.getTitle());
			data.put("description", event.getDescription());
			data.put("giver", event.getGiver());
			data.put("receiver", null);
			data.put("state", null);
			data.put("created_at", "" + event.getTimestampCreation());
			data.put("used_at", null);
			data.put("until", "" + event.getTimestampLimit());
			data.put("latitude", "" + event.getLatitude());
			data.put("longitude", "" + event.getLongitude());
			int code = HttpRequest.post(Constant.SERVER_URL + "/api/event").form(data).code();
			if (code == 200) {
				sendMessage(CODE_OK);
			} else {
				sendMessage(CODE_ERROR);
			}
			
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
