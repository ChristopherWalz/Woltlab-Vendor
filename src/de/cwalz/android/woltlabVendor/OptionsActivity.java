package de.cwalz.android.woltlabVendor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class OptionsActivity extends Activity {
	private TextView vendorIDTextView;
	private TextView apiKeyTextView;
	private SharedPreferences settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options); 
		
		vendorIDTextView = (TextView) findViewById(R.id.vendorID);
		apiKeyTextView = (TextView) findViewById(R.id.apiKey);
		settings = getApplicationContext().getSharedPreferences(WidgetProvider.PREFS_NAME, 0);
        int vendorID = settings.getInt("vendorID", 0);
        String apiKey = settings.getString("apiKey", "");
        
        if (vendorID != 0) {
        	vendorIDTextView.setText(String.valueOf(vendorID));
        }
        if (!apiKey.isEmpty()) {
        	apiKeyTextView.setText(apiKey);
        }
		
		
		// config
		Button button = (Button) findViewById(R.id.save);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {

				Spinner currencySpinner = (Spinner) findViewById(R.id.currencySpinner);
				int vendorID = 0;
				try {
					vendorID = Integer.parseInt(vendorIDTextView.getText().toString().trim());
				}
				catch (NumberFormatException e) {}
				String apiKey = apiKeyTextView.getText().toString().trim();
				String currency = currencySpinner.getSelectedItem().toString();
				
				if (vendorID == 0 || apiKey.isEmpty() || currency.isEmpty()) {
					Toast.makeText(getApplicationContext(), getString(R.string.emptyForm), Toast.LENGTH_LONG).show();
				}
				else {
					// save new values in preferences
					
				    SharedPreferences.Editor editor = settings.edit();
				    editor.putInt("vendorID", vendorID);
				    editor.putString("apiKey", apiKey);
				    editor.putString("currency", currency);
				    editor.commit();
				    
			    	/*AppWidgetManager man = AppWidgetManager.getInstance(getApplicationContext());
			    	int[] appWidgetIds = man.getAppWidgetIds(new ComponentName(getApplicationContext(), WidgetProvider.class));
			    	if (appWidgetIds.length == 0) {
			    		TransactionsUtil.update(getApplicationContext(), new ICallback() {
							public void onSuccess(float newBalance){}
							public void onFailure(String error){}
			    		});
			    	}
			    	else {
			    		WidgetProvider.forceWidgetUpdate(getApplicationContext());
			    	}*/
					
					
					
	                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
	                intent.putExtra(MainActivity.REDIRECT_OPTIONS, true);
	                startActivity(intent);
				}
					
			}
		});
	}

}
