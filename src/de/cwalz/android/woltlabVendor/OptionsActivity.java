package de.cwalz.android.woltlabVendor;

import java.text.NumberFormat;
import java.text.ParseException;

import util.AlarmUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class OptionsActivity extends Activity {
	private TextView vendorIDTextView;
	private TextView apiKeyTextView;
	private Spinner intervalView;
	private CheckBox vibrationView;
	private SharedPreferences settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		vendorIDTextView = (TextView) findViewById(R.id.vendorID);
		apiKeyTextView = (TextView) findViewById(R.id.apiKey);
		intervalView = (Spinner) findViewById(R.id.widgetInterval);
		vibrationView = (CheckBox) findViewById(R.id.vibration);
		settings = getApplicationContext().getSharedPreferences(WidgetProvider.PREFS_NAME, 0);
		int vendorID = settings.getInt("vendorID", 0);
		String apiKey = settings.getString("apiKey", "");
		int interval = settings.getInt("interval", 20);
		boolean vibration = settings.getBoolean("vibration", false);

		if (vendorID != 0) {
			vendorIDTextView.setText(String.valueOf(vendorID));
		}
		if (!apiKey.isEmpty()) {
			apiKeyTextView.setText(apiKey);
		}

		if (vibration) {
			vibrationView.setChecked(true);
		}

		switch (interval) {
			case 5:
				intervalView.setSelection(0);
			break;

			case 10:
				intervalView.setSelection(1);
			break;

			case 20:
				intervalView.setSelection(2);
			break;

			case 30:
				intervalView.setSelection(3);
			break;

			case 60:
				intervalView.setSelection(4);
			break;
		}

		// config
		Button button = (Button) findViewById(R.id.save);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {

				Spinner currencyView = (Spinner) findViewById(R.id.currency);
				int vendorID = 0;
				try {
					vendorID = Integer.parseInt(vendorIDTextView.getText().toString().trim());
				} catch (NumberFormatException e) {
				}
				String apiKey = apiKeyTextView.getText().toString().trim();
				String currency = currencyView.getSelectedItem().toString();

				// get interval time
				int interval = 20;
				try {
					interval = NumberFormat.getInstance().parse(intervalView.getSelectedItem().toString()).intValue();
				} catch (ParseException e) {
				}

				boolean vibration = vibrationView.isChecked();

				if (vendorID == 0 || apiKey.isEmpty() || currency.isEmpty()) {
					Toast.makeText(getApplicationContext(), getString(R.string.emptyForm), Toast.LENGTH_SHORT).show();
				} else {
					// save new values in preferences
					SharedPreferences.Editor editor = settings.edit();
					editor.putInt("vendorID", vendorID);
					editor.putString("apiKey", apiKey);
					editor.putString("currency", currency);
					editor.putInt("interval", interval);
					editor.putBoolean("vibration", vibration);
					editor.commit();

					// save new alarm
					AlarmUtil.start(WidgetProvider.ALARM_ID, interval, getApplicationContext());

					Intent intent = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(intent);
				}
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
