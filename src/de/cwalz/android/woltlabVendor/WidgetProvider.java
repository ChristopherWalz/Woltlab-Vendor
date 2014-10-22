package de.cwalz.android.woltlabVendor;

import util.TransactionsUtil;
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
    
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
    	super.onUpdate(context, appWidgetManager, appWidgetIds);
    	
        for (int appWidgetID : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                                                      R.layout.widget);

            remoteViews.setOnClickPendingIntent(R.id.layoutContainer,
                                                TransactionsUtil.getPendingSelfIntent(context,
                                                           ACTION_UPDATE_CLICK)
            );

            appWidgetManager.updateAppWidget(appWidgetID, remoteViews);
        }
        
        SharedPreferences settings = context.getSharedPreferences(WidgetProvider.PREFS_NAME, 0);        
        final int vendorID = settings.getInt("vendorID", 0);
        final String apiKey = settings.getString("apiKey", "");
        
        // return false if configuration is not done yet
        if (vendorID == 0 || apiKey.isEmpty()) {
        	Log.i(WidgetProvider.LOG_TAG, "Configuration not done yet, return");
        	return;
        }
        
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

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ACTION_UPDATE_CLICK.equals(intent.getAction())) {
        	Log.i("CLICKED", ACTION_UPDATE_CLICK);
            Intent mainIntent = new Intent(context, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainIntent);
        }
    }
}