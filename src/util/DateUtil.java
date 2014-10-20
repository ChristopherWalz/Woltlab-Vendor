package util;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.text.format.DateUtils;

public final class DateUtil {

	public final static CharSequence getRelativeDate(int timestamp) {
        CharSequence time = DateUtils.getRelativeTimeSpanString((long) timestamp*1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

        return time;
	}
	
	public final static String getDate(int timestamp) {
		Date date = new Date(timestamp*1000L); // *1000 is to convert seconds to milliseconds
		DateFormat format = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT);
		
		return format.format(date);

	}
	
	public final static long getTimestamp() {
		return System.currentTimeMillis() / 1000;
	}
}
