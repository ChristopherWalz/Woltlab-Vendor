package de.cwalz.android.woltlabVendor;

import java.text.NumberFormat;
import java.text.ParseException;

import util.AlarmUtil;
import util.TransactionsUtil;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class WidgetConfigure extends Activity {
	private int mAppWidgetId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);
		setResult(RESULT_CANCELED);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		// No valid ID, so bail out.
		if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			finish();
		}

		// Check if prefs are already set, finish with RESULT_OK
		SharedPreferences settings = getSharedPreferences(WidgetProvider.PREFS_NAME, 0);
		int vendorID = settings.getInt("vendorID", 0);
		String apiKey = settings.getString("apiKey", "");
		float balance = settings.getFloat("balance", 0);

		if (vendorID != 0 && !apiKey.isEmpty()) {
			TransactionsUtil.updateBalance(String.valueOf(balance), this);
			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
			setResult(RESULT_OK, resultValue);
			finish();
		}

		// config
		Button button = (Button) findViewById(R.id.save);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				TextView vendorIDTextView = (TextView) findViewById(R.id.vendorID);
				TextView apiKeyTextView = (TextView) findViewById(R.id.apiKey);
				Spinner currencySpinner = (Spinner) findViewById(R.id.currency);
				Spinner intervalView = (Spinner) findViewById(R.id.widgetInterval);
				CheckBox vibrationView = (CheckBox) findViewById(R.id.vibration);

				int vendorID = !vendorIDTextView.getText().toString().isEmpty() ? Integer.parseInt(vendorIDTextView
						.getText().toString().trim()) : 0;
				String apiKey = apiKeyTextView.getText().toString().trim();
				String currency = currencySpinner.getSelectedItem().toString();

				// get interval time
				int interval = 20;
				try {
					interval = NumberFormat.getInstance().parse(intervalView.getSelectedItem().toString()).intValue();
				} catch (ParseException e) {
				}

				boolean vibration = vibrationView.isChecked();

				if (vendorID == 0 || apiKey.isEmpty() || currency.isEmpty()) {
					Toast.makeText(WidgetConfigure.this, getString(R.string.emptyForm), Toast.LENGTH_SHORT).show();
				} else {
					// save new values in preferences
					SharedPreferences settings = WidgetConfigure.this
							.getSharedPreferences(WidgetProvider.PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putInt("vendorID", vendorID);
					editor.putString("apiKey", apiKey);
					editor.putString("currency", currency);
					editor.putInt("interval", interval);
					editor.putBoolean("vibration", vibration);
					editor.commit();

					// save new alarm
					AlarmUtil.start(WidgetProvider.ALARM_ID, interval, WidgetConfigure.this);

					// Push widget update to surface with newly set prefix
					AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(WidgetConfigure.this);
					WidgetProvider.updateAppWidget(WidgetConfigure.this, appWidgetManager, new int[] { mAppWidgetId });

					// Make sure we pass back the original appWidgetId
					Intent resultValue = new Intent();
					resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
					setResult(RESULT_OK, resultValue);
					finish();
				}

			}
		});
	}

}
