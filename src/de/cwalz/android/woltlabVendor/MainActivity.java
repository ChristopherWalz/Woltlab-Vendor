package de.cwalz.android.woltlabVendor;

import java.util.ArrayList;
import java.util.List;

import sql.TransactionsDataSource;
import util.NetworkUtil;
import util.TransactionsUtil;
import adapter.TransactionsAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import data.Transaction;

public class MainActivity extends ListActivity {
	private TransactionsDataSource datasource;
	TransactionsAdapter transactionsAdapter;
	List<Transaction> transactions = new ArrayList<Transaction>();
	float balance = 0;
	String currency = "EUR";
	SharedPreferences settings;

	// views
	TextView balanceView;
	ProgressBar loadingView;
	TextView emptyView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// views
		loadingView = (ProgressBar) findViewById(R.id.loading);
		balanceView = (TextView) findViewById(R.id.balance);
		emptyView = (TextView) getListView().getEmptyView();

		datasource = new TransactionsDataSource(this);
		datasource.open();

		Transaction lastTransaction = datasource.getLastTransaction();

		// set current balance and currency
		balance = lastTransaction.getBalance();
		settings = getSharedPreferences(WidgetProvider.PREFS_NAME, 0);
		currency = settings.getString("currency", "EUR");

		transactions.addAll(datasource.getList());
		datasource.close();

		transactionsAdapter = new TransactionsAdapter(this, R.layout.transaction_list, transactions);
		setListAdapter(transactionsAdapter);

		int vendorID = settings.getInt("vendorID", 0);
		String apiKey = settings.getString("apiKey", "");

		// return false if configuration is not done yet
		if (vendorID == 0 || apiKey.isEmpty()) {
			Log.i(WidgetProvider.LOG_TAG, "Configuration not done yet, return");
			return;
		}

		refresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
			case R.id.action_settings:
				Intent intent = new Intent(this, OptionsActivity.class);
				startActivity(intent);
				return true;
			case R.id.action_refresh:
				refresh();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void refresh() {
		// return if no network is available
		if (!NetworkUtil.hasInternetConnection(this)) {
			return;
		}

		getListView().setVisibility(View.GONE);
		balanceView.setVisibility(View.GONE);
		emptyView.setVisibility(View.GONE);
		loadingView.setVisibility(View.VISIBLE);

		// get new balance from shared preferences (necessary because onCreate
		// will only be called once and not when action_refresh is clicked)
		datasource.open();
		Transaction lastTransaction = datasource.getLastTransaction();
		datasource.close();
		balance = lastTransaction.getBalance();
		balanceView.setText(getString(R.string.currentBalance) + " " + currency + " " + balance);

		TransactionsUtil.update(this, new ICallback() {
			public void onSuccess(final float newBalance) {
				// clear current list
				transactions.clear();

				datasource.open();
				transactions.addAll(datasource.getList());
				datasource.close();

				runOnUiThread(new Runnable() {
					public void run() {
						transactionsAdapter.notifyDataSetChanged();

						// show views
						loadingView.setVisibility(View.GONE);
						getListView().setVisibility(View.VISIBLE);
						balanceView.setVisibility(View.VISIBLE);

						if (transactions.size() == 0) {
							emptyView.setVisibility(View.VISIBLE);
						} else {
							if (newBalance > balance) {
								// update view
								balanceView.setText(getString(R.string.currentBalance) + " " + currency + " "
										+ newBalance);

								// update widget
								TransactionsUtil.updateBalance(String.valueOf(newBalance), MainActivity.this);
							}
							if (balanceView.getText().toString().isEmpty()) {
								balanceView
										.setText(getString(R.string.currentBalance) + " " + currency + " " + balance);

								// update widget
								TransactionsUtil.updateBalance(String.valueOf(balance), MainActivity.this);
							}
						}
					}
				});

			}

			public void onFailure(String error) {
				runOnUiThread(new Runnable() {
					public void run() {
						// show views
						loadingView.setVisibility(View.GONE);
						getListView().setVisibility(View.VISIBLE);
						Log.i("BALANCE", String.valueOf(balance));
						if (balance > 0) {
							balanceView.setVisibility(View.VISIBLE);
						}
						if (transactions.size() == 0) {
							emptyView.setVisibility(View.VISIBLE);
						}
					}
				});

			}
		});
	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}

}