package com.theostriches.amaretto.android;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.theostriches.amaretto.android.model.Event;
import com.theostriches.amaretto.android.model.Point;
import com.theostriches.amaretto.android.model.User;
import com.theostriches.amaretto.android.server.PostGetEventForMe;
import com.theostriches.amaretto.android.util.LocalDataManager;
import com.theostriches.amaretto.android.util.Util;

public class EventActivity extends SherlockFragmentActivity implements OnClickListener {

	private User user;
	private ArrayList<Event> eventList;
	private Context mContext;
	private EditText eUser;
	private EditText ePass;
	private LocalDataManager localData;
	private Event e;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_event);
		mContext = this;
		e = (Event) getIntent().getSerializableExtra("event");
		Point point = (Point) getIntent().getSerializableExtra("point");
		user = (User) getIntent().getSerializableExtra("user");

		TextView t1 = (TextView) findViewById(R.id.textViewTitle);
		t1.setText(e.getTitle());
		TextView t2 = (TextView) findViewById(R.id.textViewDesc);
		t2.setText(e.getDescription());
		TextView t3 = (TextView) findViewById(R.id.textViewUser);
		t3.setText("por " + e.getGiver().getName());
		TextView t4 = (TextView) findViewById(R.id.textViewMore);
		Date d = new Date();
		d.setTime(e.getTimestampLimit());
		t4.setText("hasta "
				+ d.toLocaleString()
				+ " - est‡s a "
				+ Math.round(Util.distFrom(Double.valueOf(e.getLatitude()).floatValue(), Double
						.valueOf(e.getLongitude()).floatValue(), Double
						.valueOf(point.getLatitude()).floatValue(),
						Double.valueOf(point.getLongitude()).floatValue())) + "m");
		
		Button b = (Button) findViewById(R.id.buttonGet);
		b.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case  R.id.buttonGet:
			 getEventForMe();
		default:
			break;
		}

	}

	private void getEventForMe() {
		Handler updateHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				try {
					switch (msg.what) {
					case PostGetEventForMe.CODE_OK:
						Toast.makeText(mContext, "Ok!", Toast.LENGTH_LONG).show();
						finish();
						break;
					case PostGetEventForMe.CODE_ERROR:
					default:
						Toast.makeText(mContext, "Error", Toast.LENGTH_LONG).show();
						break;
					}
				} catch (Exception e) {
					// Fragment closed, do nothing
				}
			}
		};
		PostGetEventForMe up = new PostGetEventForMe(updateHandler, user.getName(), e.getTitle() );
		up.start();
	}

}
