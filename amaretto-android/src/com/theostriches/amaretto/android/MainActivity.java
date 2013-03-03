package com.theostriches.amaretto.android;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.theostriches.amaretto.android.fragment.EventListFragment;
import com.theostriches.amaretto.android.fragment.EventMapFragment;
import com.theostriches.amaretto.android.fragment.NewEventFragment;
import com.theostriches.amaretto.android.location.GpsThread;
import com.theostriches.amaretto.android.model.Event;
import com.theostriches.amaretto.android.model.Point;
import com.theostriches.amaretto.android.model.User;
import com.theostriches.amaretto.android.server.GetEvents;
import com.theostriches.amaretto.android.util.LocalDataManager;

public class MainActivity extends SherlockFragmentActivity {

	public static final int LOGIN_REQUEST = 133;
	public static final int EVENT_REQUEST = 134;

	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;

	private User user;
	private ArrayList<Event> eventList;
	private GpsThread mGpsThread;
	private Handler mHandlerGps;
	private ProgressDialog mProgressDialog;
	private Context mContext;
	private Point location;
	private LocalDataManager mLocalDataManager;

	public Point getLocation() {
		return location;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		mLocalDataManager = new LocalDataManager(mContext);
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		setUser(mLocalDataManager.getLogin());
		// TODO from server
		eventList = new ArrayList<Event>();
		// Event e1 = new Event();
		// e1.setTitle("Evento 1");
		// Event e2 = new Event();
		// e2.setTitle("Evento 2");
		// Event e3 = new Event();
		// e3.setTitle("Evento 3");
		// eventList.add(e1);
		// eventList.add(e2);
		// eventList.add(e3);
		getEventsFromServer();

		mViewPager.setCurrentItem(1);
		getLocationFromGps();
	}




	public ArrayList<Event> getEventList() {
		return eventList;
	}

	public void setEventList(ArrayList<Event> eventList) {
		this.eventList = eventList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public static final int FRAGMENT_COUNT = 3;

		private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				if (fragments[0] == null) {
					fragments[0] = new NewEventFragment();
				}
				return fragments[0];
			case 1:
				if (fragments[1] == null) {
					fragments[1] = new EventListFragment();
				}
				return fragments[1];
			case 2:
				if (fragments[2] == null) {
					fragments[2] = new EventMapFragment();
				}
				return fragments[2];
			default:
				Fragment fragment = new DummySectionFragment();
				Bundle args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
				fragment.setArguments(args);
				return fragment;
			}

		}

		public void updateMap(Point p) {
			if (fragments[2] != null) {
				((EventMapFragment) fragments[2]).updateMap(p);
				mSectionsPagerAdapter.updateEvents();
			}
		}

		public void updateEvents() {
			if (fragments[2] != null) {
				((EventMapFragment) fragments[2]).updateEvents();
			}
			if (fragments[1] != null) {
				((EventListFragment) fragments[1]).updateEvents();
			}

		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return FRAGMENT_COUNT;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends SherlockFragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);
			TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

	private void getLocationFromGps() {
		if (mHandlerGps == null) {
			mHandlerGps = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// mProgressDialog.dismiss();
					workFinished();
					switch (msg.what) {
					case GpsThread.GEOCODE_OK:
					case GpsThread.GEOCODE_OK_FIRST:
						// Toast.makeText(mContext,
						// mContext.getText(R.string.gps_signal_found),
						// Toast.LENGTH_SHORT).show();
						Location loc = (Location) msg.obj;
						location = new Point(loc.getLatitude(), loc.getLongitude());
						mSectionsPagerAdapter.updateMap(location);
						break;
					case GpsThread.GEOCODE_GPS_FAILED:
						Toast.makeText(mContext, mContext.getString(R.string.gps_signal_not_found),
								Toast.LENGTH_LONG).show();
						break;
					case GpsThread.GEOCODE_OFF:
						Toast.makeText(mContext, mContext.getString(R.string.gps_signal_not_found),
								Toast.LENGTH_LONG).show();
						break;
					}
				}
			};
		}
		mGpsThread = new GpsThread(mHandlerGps, mContext, false);
		mGpsThread.start();
		// mProgressDialog = ProgressDialog.show(mContext,
		// mContext.getText(R.string.please_wait),
		// mContext.getText(R.string.search_signal_gps), true, true, null);

	}

	private void workFinished() {
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case LOGIN_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
			}
			break;

		default:
			break;
		}
	}

	private void getEventsFromServer() {
		Handler updateHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				try {
					switch (msg.what) {
					case GetEvents.CODE_OK:
						eventList = (ArrayList<Event>) msg.obj;
						mSectionsPagerAdapter.updateEvents();
						break;
					case GetEvents.CODE_ERROR:
					default:
						// Show error
						break;
					}
				} catch (Exception e) {
					// Fragment closed, do nothing
				}
			}
		};
		GetEvents up = new GetEvents(updateHandler);
		up.start();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu submenu = menu.addSubMenu(0, Menu.NONE, 1, R.string.menu_more).setIcon(R.drawable.ic_menu_more);
		submenu.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		submenu.add(0, 1, Menu.NONE, "Usuario");
	    return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case 1:
			if (mLocalDataManager.getLogin() == null) {
				showLogin();
			} else {
				mLocalDataManager.clearLogin();
				Toast.makeText(mContext, "LogOut realizado", Toast.LENGTH_LONG).show();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void showLogin() {
		Intent i = new Intent(mContext, LoginActivity.class);
		startActivityForResult(i, LOGIN_REQUEST);
	}
	
	public void showEventActivity(Event e) {
		Intent i = new Intent(mContext, EventActivity.class);
		i.putExtra("event", e);
		i.putExtra("point", location);
		i.putExtra("user", user);
		startActivityForResult(i, EVENT_REQUEST);
	}
	
	public void updateEvents() {
		getEventsFromServer();
	}


}
