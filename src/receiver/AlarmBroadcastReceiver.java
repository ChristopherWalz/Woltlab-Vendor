package receiver;

import de.cwalz.android.woltlabVendor.WidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
	    // Update the widget
		WidgetProvider.forceWidgetUpdate(context);
	}	

}
