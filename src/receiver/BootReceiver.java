package receiver;

import util.AlarmUtil;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import de.cwalz.android.woltlabVendor.WidgetProvider;

public class BootReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {  
        SharedPreferences settings = context.getSharedPreferences(WidgetProvider.PREFS_NAME, 0);
	    int interval = settings.getInt("interval", 20);
	    
	    // restart alarm
	    AlarmUtil.start(WidgetProvider.ALARM_ID, interval, context);
    }
}