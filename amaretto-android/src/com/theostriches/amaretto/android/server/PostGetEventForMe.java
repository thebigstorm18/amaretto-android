package com.theostriches.amaretto.android.server;

import java.util.HashMap;
import java.util.Map;

import android.os.Handler;
import android.os.Message;

import com.theostriches.amaretto.android.model.User;
import com.theostriches.amaretto.android.util.Constant;
import com.theostriches.amaretto.android.util.Log;

/**
 * @author Antonio Prada <toniprada@gmail.com>
 * 
 * 
 */
public class PostGetEventForMe extends Thread {

	public static final int CODE_OK = 303;
	public static final int CODE_BADAUTH = 320;
	public static final int CODE_ERROR = 351;

	private Handler mHandler;
	private String usernameReceiver;
	private String titleEvent;

	private boolean done = false;

	public PostGetEventForMe(Handler handler, String usernameReceiver, String titleEvent) {
		this.mHandler = handler;
		this.usernameReceiver  = usernameReceiver;
		this.titleEvent =  titleEvent;
		
	}

	@Override
	public void run() {
		try {			
			Map<String, String> data = new HashMap<String, String>();
			data.put("title", titleEvent);
			Log.i("title:"+ titleEvent);
			data.put("receiver", usernameReceiver);
			Log.i("receiver:" + usernameReceiver);
			HttpRequest h = HttpRequest.post(Constant.SERVER_URL + "/api/event/receive");
			Log.i(h.body());
			int code = h.form(data).code();
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
