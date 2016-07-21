package com.esp.socialintegrationdemo.fblogin;

import android.content.Context;
import android.net.ConnectivityManager;

public class Utils {
	public static boolean isOnline(Context context) {
		try {
			if (context == null)
				return false;

			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (cm != null) {
				if (cm.getActiveNetworkInfo() != null) {
					return cm.getActiveNetworkInfo().isConnected();
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			// Log.error("Exception", e);
			return false;
		}
	}
}
