package com.theostriches.amaretto.android.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

	/**
	 * This must be false for production. If true, turns on logging, test code,
	 * etc.
	 */
	static final boolean DEBUG = true;
	public final static String LOGTAG = "mutequi";

	public static void v(String logMe) {
		if (DEBUG) {
			android.util.Log.v(LOGTAG, /* SystemClock.uptimeMillis() + " " + */
			logMe);
		}
	}
	
	public static void d(String logMe) {
		if (DEBUG) {
			android.util.Log.d(LOGTAG, logMe);
		}
	}

	public static void i(String logMe) {
		if (DEBUG) {
			android.util.Log.i(LOGTAG, logMe);
		}
	}
	public static void w(String logMe) {
		if (DEBUG) {
			android.util.Log.w(LOGTAG, logMe);
		}
	}

	public static void e(String logMe) {
		if (DEBUG) {
			android.util.Log.e(LOGTAG, logMe);
		}
	}

	public static void e(String logMe, Exception ex) {
		if (DEBUG) {
			android.util.Log.e(LOGTAG, logMe, ex);
		}
	}

	public static String formatTime(long millis) {
		return new SimpleDateFormat("HH:mm:ss.SSS aaa")
				.format(new Date(millis));
	}
	
}
