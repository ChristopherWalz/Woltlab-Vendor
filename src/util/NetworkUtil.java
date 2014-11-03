package util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
	public static boolean hasInternetConnection(Context context) {
		// http://developer.android.com/training/basics/network-ops/connecting.html#connection
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
		    return true;
		}
		else {
			return false;
		}		
	}
}
