package com.theostriches.amaretto.android.fragment;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.theostriches.amaretto.android.MainActivity;
import com.theostriches.amaretto.android.R;
import com.theostriches.amaretto.android.model.Event;
import com.theostriches.amaretto.android.server.NewEvent;
import com.theostriches.amaretto.android.server.PostLogIn;
import com.theostriches.amaretto.android.util.LocalDataManager;

public class NewEventFragment extends SherlockFragment {

	private MainActivity mMain;
	private Event event;
	private EditText et;
	private EditText et2;
	private LocalDataManager mLocalData;
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mMain = (MainActivity) activity;
		event = new Event();
		mLocalData = new LocalDataManager(mMain);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_newevent, container, false);
		Button b = (Button) v.findViewById(R.id.buttonDate);
		et = (EditText) v.findViewById(R.id.editTextTitle);
		et2 = (EditText) v.findViewById(R.id.editTextDesc);

		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				// Get the layout inflater
				LayoutInflater inflater = getActivity().getLayoutInflater();

				// Inflate and set the layout for the dialog
				// Pass null as the parent view because its going in the dialog
				// layout
				final View dialogView = inflater.inflate(R.layout.dialog_date, null);
				builder.setView(dialogView)
				// Add action buttons
						.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								DatePicker d = (DatePicker) dialogView.findViewById(R.id.datePicker1);
								TimePicker t = (TimePicker) dialogView.findViewById(R.id.timePicker1);
								int month = d.getMonth();
								int dayOfMonth = d.getDayOfMonth();
								int year = d.getYear();
								int hour = t.getCurrentHour();
								int minute = t.getCurrentMinute();
							    Calendar cal = Calendar.getInstance();
							    cal.set(Calendar.YEAR, year);
							    cal.set(Calendar.MONTH, month);
							    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
							    cal.set(Calendar.HOUR_OF_DAY, hour);
							    cal.set(Calendar.MINUTE, minute);
							    Date dateRepresentation = cal.getTime();
							    event.setTimestampLimit(dateRepresentation.getTime());
							}
						});
				builder.create().show();
			}
		});
		Button bs = (Button) v.findViewById(R.id.buttonSubmit);
		bs.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				event.setTitle(et.getText().toString());
				event.setDescription(et2.getText().toString());
				event.setLatitude(mMain.getLocation().getLatitude());
				event.setLongitude(mMain.getLocation().getLongitude());
				if (mLocalData.getLogin() == null) {
					mMain.showLogin();
				} else {
					event.setGiver(mLocalData.getLogin());
					sendEvent(event);
				}
			}
		});
		return v;
		
	}

	private void sendEvent(Event event) {
		Handler updateHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				try {
					switch (msg.what) {
					case NewEvent.CODE_OK:
						Toast.makeText(mMain, "Evento enviado correctamente",
								Toast.LENGTH_LONG).show();
						et.setText("");
						et2.setText("");
						mMain.updateEvents();
						break;
					case NewEvent.CODE_ERROR:
					default:
						Toast.makeText(mMain, "Fallo al enviar evento",
								Toast.LENGTH_LONG).show();
						break;
					}
				} catch (Exception e) {
					// Fragment closed, do nothing
				}
			}
		};
		NewEvent up = new NewEvent(updateHandler, event);
		up.start();
	}

}
