package com.theostriches.amaretto.android.location;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.theostriches.amaretto.android.util.Log;

/**
 * Thread que se encarga de obtener la geolocalizacion
 * 
 * @author Antonio Prada
 */
public class GpsThread extends Thread {

	public static final int GEOCODE_OK = 111;
	public static final int GEOCODE_OK_FIRST = 112;
	public static final int GEOCODE_OFF = 114;
	public static final int GEOCODE_GPS_FAILED = 113;

	private LocationManager mManager;
	private Context mContext;
	private Handler mHandler;
	private CustomLocationListener mListener;
	private boolean mIsFirstUpdate;
	private boolean mCancelled = false;

	/**
	 * Crea el hilo de ejecucion, guardando el handler para la comunicacin con
	 * la hebra grfica y tambien si es o no la primera vez que se le llama (al
	 * inicio de la aplicacion es necesario obtener la localizacion rapidamente,
	 * y esto se puede hacer usando la ultima conocida).
	 * 
	 * @param handler
	 *            Handler para comunicarse con la hebra grfica.
	 * @param context
	 *            Contexto de la aplicacin.
	 * @param isFirstUpdate
	 *            Si estamos en el inicio de la aplicacin.
	 */
	public GpsThread(Handler handler, Context context, boolean isFirstUpdate) {
		mContext = context;
		mHandler = handler;
		mIsFirstUpdate = isFirstUpdate;
		if (mManager == null) {
			mManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		}
		if (mListener == null) {
			mListener = new CustomLocationListener();
		}
	}

	/**
	 * Obtiene el mejor network provider posible de entre los activados, segun
	 * si se esta llamando al hilo desde el inicio de la aplicacin o no.
	 * 
	 * @return String con el mejor network provider para esta situacion
	 */
	private String getBestProvider() {
		Criteria criteria = new Criteria();
		criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
		criteria.setAltitudeRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		if (mIsFirstUpdate) {
			// TODO fine o coarse? rapidez vs exactitud...
			criteria.setAccuracy(Criteria.ACCURACY_COARSE); 
		} else {
			criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		}
		return mManager.getBestProvider(criteria, true);
	}

	/**
	 * Enva la localizacin a la hebra grfica.
	 * 
	 * @param location
	 *            Localizacin actual.
	 */
	private void setLocation(Location location) {
		int code;
//		if (mIsFirstUpdate) {
//			code = GEOCODE_OK_FIRST;
//		} else {
			code = GEOCODE_OK;
//		}
		Message msg = Message.obtain(mHandler, code, location);
		mHandler.sendMessage(msg);
	}

	/**
	 * Inicia el hilo de ejecucin.
	 */
	@Override
	public void run() {
		if (mIsFirstUpdate) {
			Log.d("Initial location update");
			String provider = getBestProvider();
			if (provider != null && !provider.equals("")) {
				Location location = mManager.getLastKnownLocation(provider);
				if (location != null) {
					setLocation(location);
				}
			}
		} else {
			Log.d("Normal location update");
			String provider = getBestProvider();
			if (provider != null && !provider.equals("")) {
				Looper.prepare();
				mManager.requestLocationUpdates(provider, 1000L, 500.0f, mListener);
				Looper.loop();
				Looper.myLooper().quit();
			} else {
				mHandler.sendEmptyMessage(GEOCODE_OFF);
			}
		}
	}

	/**
	 * Listener de las actualizaciones de la localizacin. Solo nos interesa la
	 * primera actualizacin y entonces dejaremos de escuchar.
	 * 
	 * @author Antonio Prada
	 */
	public class CustomLocationListener implements LocationListener {

		/**
		 * Cuando tengamos una nueva localizacin.
		 */
		public void onLocationChanged(Location location) {
			Log.i("We have the location");
			mManager.removeUpdates(mListener);
			if (!interrupted() && !mCancelled) {
				if (location != null) {
					setLocation(location);
				} else {
					mHandler.sendEmptyMessage(GEOCODE_GPS_FAILED);
				}
			} else {
				Log.d("Cancelled while listening for a location");
				return;
			}
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

	}

	/**
	 * Cancelar la busqueda de la localizacin.
	 */
	public void cancel() {
		mCancelled = true;
		mManager.removeUpdates(mListener);
	}

}