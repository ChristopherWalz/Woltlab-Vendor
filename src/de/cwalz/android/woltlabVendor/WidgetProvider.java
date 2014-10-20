package de.cwalz.android.woltlabVendor;

import util.TransactionsUtil;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class WidgetProvider extends AppWidgetProvider {
    public static final String ACTION_UPDATE_CLICK = "de.cwalz.android.woltlabVendor.UPDATE_CLICK";	
    public static final String PREFS_NAME = "de.cwalz.android.woltlabVendor.PREFS";
    public static final String LOG_TAG = "de.cwalz.android.woltlabVendor.LOG_TAG";
    public static final String LOADING_STRING = "...";
    
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
    	super.onUpdate(context, appWidgetManager, appWidgetIds);
    	
       /* for (int appWidgetID : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                                                      R.layout.widget);

            remoteViews.setOnClickPendingIntent(R.id.layout,
                                                TransactionsUtil.getPendingSelfIntent(context,
                                                           ACTION_UPDATE_CLICK)
            );

            appWidgetManager.updateAppWidget(appWidgetID, remoteViews);
        }*/
        
        
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
    
    /**
     * A general technique for calling the onUpdate method,
     * requiring only the context parameter.
     *
     * @author John Bentley, based on Android-er code.
     * @see <a href="http://android-er.blogspot.com
     * .au/2010/10/update-widget-in-onreceive-method.html">
     * Android-er > 2010-10-19 > Update Widget in onReceive() method</a>
     */
    private void onUpdate(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance
                (context);

        // Uses getClass().getName() rather than MyWidget.class.getName() for
        // portability into any App Widget Provider Class
        ComponentName thisAppWidgetComponentName =
                new ComponentName(context.getPackageName(),getClass().getName()
        );
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                thisAppWidgetComponentName);
        onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ACTION_UPDATE_CLICK.equals(intent.getAction())) {
            onUpdate(context);
        }
    }
}