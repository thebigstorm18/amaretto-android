package com.theostriches.amaretto.android.fragment;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.theostriches.amaretto.android.EventActivity;
import com.theostriches.amaretto.android.MainActivity;
import com.theostriches.amaretto.android.R;
import com.theostriches.amaretto.android.model.Event;
import com.theostriches.amaretto.android.model.Point;
import com.theostriches.amaretto.android.util.Log;
import com.theostriches.amaretto.android.util.Util;

public class EventListFragment extends SherlockListFragment implements OnItemClickListener {

	private MainActivity mMain;

	private ItemListAdapter adapter;
	private TextView emptyT;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mMain = (MainActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_products, container, false);
		adapter = new ItemListAdapter(mMain.getEventList());
		emptyT = (TextView) v.findViewById(android.R.id.empty);
		setListAdapter(adapter);
		return v;
	}

	@Override
	public void onStart() {
		getListView().setEmptyView(emptyT);
		getListView().setOnItemClickListener(this);
		super.onStart();
	}

	public class ItemListAdapter extends ArrayAdapter<Event> {

		private ArrayList<Event> mListProducts;

		public ItemListAdapter(ArrayList<Event> listLibros) {
			super(mMain, 0);
			this.mListProducts = listLibros;
		}

		public int getCount() {
			return mListProducts.size();
		}

		public Event getItem(int position) {
			return mListProducts.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View v;
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mMain
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.fragment_products_row, null);
			} else {
				v = convertView;
			}
			final Event p = getItem(position);
			if (p != null) {
				TextView tt = (TextView) v.findViewById(R.id.textViewTitle);
				if (tt != null) {
					tt.setText(p.getTitle());
				}
				TextView td = (TextView) v.findViewById(R.id.textViewDesc);
				if (td != null) {
					td.setText(p.getDescription());
				}
				TextView tu = (TextView) v.findViewById(R.id.textViewLimit);
				if (tu != null) {
					Date date = new Date();
					date.setTime(p.getTimestampLimit());
					tu.setText("Hasta " + date.toLocaleString());
				}
				TextView tdd = (TextView) v.findViewById(R.id.textViewDistance);
				if (tdd != null) {
					Point point = mMain.getLocation();
					tdd.setText("a "
							+ Math.round(Util.distFrom(
									Double.valueOf(p.getLatitude()).floatValue(),
									Double.valueOf(p.getLongitude()).floatValue(),
									Double.valueOf(point.getLatitude()).floatValue(), Double
											.valueOf(point.getLongitude()).floatValue())) + "m");
				}
			}
			return v;
		}
	}

	public void updateEvents() {
		adapter = new ItemListAdapter(mMain.getEventList());
		setListAdapter(adapter);

		// adapter.notifyDataSetChanged();
		Log.i("updateEvents");
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Event e = mMain.getEventList().get(arg2);
		Point p = mMain.getLocation();
		Intent i = new Intent(mMain, EventActivity.class);
		i.putExtra("event", e);
		i.putExtra("point", p);
		i.putExtra("user", mMain.getUser());
		startActivity(i);
	}

}
