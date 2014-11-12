package util;

import java.util.Calendar;

import receiver.AlarmBroadcastReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmUtil {
	public static void start(int alarmID, int minutes, Context context) {
		int milliseconds = minutes * 60 * 1000;
		Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
		PendingIntent pendIntent = PendingIntent.getBroadcast(context, alarmID, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendIntent); // cancel if active already
		alarmManager.setRepeating(AlarmManager.RTC, Calendar.getInstance().getTimeInMillis() + milliseconds,
				milliseconds, pendIntent);
	}
}
