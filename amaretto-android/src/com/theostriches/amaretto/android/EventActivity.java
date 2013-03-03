package com.theostriches.amaretto.android;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.theostriches.amaretto.android.model.Event;
import com.theostriches.amaretto.android.model.User;
import com.theostriches.amaretto.android.server.PostLogIn;
import com.theostriches.amaretto.android.util.LocalDataManager;

public class EventActivity extends SherlockFragmentActivity implements OnClickListener {
	
	private User user;
	private ArrayList<Event> eventList;
	private Context mContext;
	private EditText eUser;
	private EditText ePass;
	private LocalDataManager localData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mContext = this;
		localData = new LocalDataManager(mContext);
		eUser = (EditText) findViewById(R.id.editTextUser);
		ePass = (EditText) findViewById(R.id.editTextPass);
		Button b = (Button) findViewById(R.id.buttonLogin);
		b.setOnClickListener(this);
		Button b2 = (Button) findViewById(R.id.buttonSignUp);
		b2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonLogin:
			login(false);
			break;
		case R.id.buttonSignUp:
			login(true);
			break;
		default:
			break;
		}

	}

	private void login(boolean newUser) {
		Handler updateHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				try {
					switch (msg.what) {
					case PostLogIn.CODE_OK:
						User user = (User) msg.obj;
						localData.setUser(user);
						setResult(Activity.RESULT_OK);
						finish();
						break;
					case PostLogIn.CODE_BADAUTH:
						Toast.makeText(mContext, "Login no autorizado",
								Toast.LENGTH_LONG).show();
						break;
					case PostLogIn.CODE_ERROR:
					default:
						// Show error
						break;
					}
				} catch (Exception e) {
					// Fragment closed, do nothing
				}
			}
		};
		PostLogIn up = new PostLogIn(updateHandler, eUser.getText().toString(), md5(ePass.getText()
				.toString()), newUser);
		up.start();
	}

	public String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++)
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

}
