package de.cwalz.android.woltlabVendor;

import util.TransactionsUtil;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {
    public static final String ACTION_UPDATE_CLICK = "de.cwalz.android.woltlabVendor.UPDATE_CLICK";	
    public static final String PREFS_NAME = "de.cwalz.android.woltlabVendor.PREFS";
    public static final String LOG_TAG = "de.cwalz.android.woltlabVendor.LOG_TAG";
    public static final String LOADING_STRING = "...";
    public static final int ALARM_ID = 6969;
    
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
    	super.onUpdate(context, appWidgetManager, appWidgetIds);
    	
        for (int appWidgetID : appWidgetIds) {
            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setOnClickPendingIntent(R.id.layoutContainer, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetID, views);
        }
        
        SharedPreferences settings = context.getSharedPreferences(WidgetProvider.PREFS_NAME, 0);        
        final int vendorID = settings.getInt("vendorID", 0);
        final String apiKey = settings.getString("apiKey", "");
        
        // return false if configuration is not done yet
        if (vendorID == 0 || apiKey.isEmpty()) {
        	Log.i(WidgetProvider.LOG_TAG, "Configuration not done yet, return");
        	return;
        }        
        
        // update widget
        TransactionsUtil.updateBalance(LOADING_STRING, context);
        TransactionsUtil.update(context, new ICallback() {
        	public void onSuccess(float newBalance) {
        		if (newBalance != 0) {
        			TransactionsUtil.updateBalance(String.valueOf(newBalance), context);
        		}
        		else {
        			SharedPreferences settings = context.getSharedPreferences(WidgetProvider.PREFS_NAME, 0);
        		    float balance = settings.getFloat("balance", 0.f);
        			TransactionsUtil.updateBalance(String.valueOf(balance), context);
        		}
        	}
			public void onFailure(String error) {}
        });
    }
    
    public static void forceWidgetUpdate(Context context) {
    	AppWidgetManager man = AppWidgetManager.getInstance(context);
    	int[] appWidgetIds = man.getAppWidgetIds(new ComponentName(context, WidgetProvider.class));

		context.sendBroadcast(new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
			.setPackage(context.getPackageName())
			.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, 
				appWidgetIds
			));
	}
}