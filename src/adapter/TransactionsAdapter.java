package adapter;

import java.util.ArrayList;
import java.util.List;

import util.DateUtil;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import data.Transaction;
import de.cwalz.android.woltlabVendor.R;
import de.cwalz.android.woltlabVendor.WidgetProvider;

public class TransactionsAdapter extends ArrayAdapter<Transaction> {
	List<Transaction> transactions = new ArrayList<Transaction>();
	private Context context;
	int resource;
	String currency;
	

	public TransactionsAdapter(Context context, int resource, List<Transaction> transactions) {
		super(context, resource);
		this.context = context;
		this.resource = resource;
		this.transactions = transactions;
		
        // get current balance
        SharedPreferences settings = context.getSharedPreferences(WidgetProvider.PREFS_NAME, 0);
        currency = settings.getString("currency", "EUR");
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {     
		View row;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(resource, parent, false);
		} else {
			row = convertView;
		}

        Transaction transaction = transactions.get(position);
        TextView dateView = (TextView) row.findViewById(R.id.date);
        TextView balanceView = (TextView) row.findViewById(R.id.balance);
        TextView reasonView = (TextView) row.findViewById(R.id.reason);
        RelativeLayout layout = (RelativeLayout) row.findViewById(R.id.layout);
        
        if (transaction.isWithdrawal()) {
        	layout.setBackgroundColor(Color.parseColor("#BACF16"));
        	dateView.setTextColor(Color.BLACK);
        	balanceView.setTextColor(Color.BLACK);
        	reasonView.setTextColor(Color.BLACK);
        }
        else {
        	dateView.setTextColor(Color.WHITE);
        	balanceView.setTextColor(Color.WHITE);
        	reasonView.setTextColor(Color.WHITE);
        	
        	if (position % 2 == 0 ) {
        		layout.setBackgroundColor(Color.BLACK);
        	}
        	else {
        		layout.setBackgroundColor(Color.parseColor("#121212"));
        	}
        }

        dateView.setText(DateUtil.getDate((transaction.getTime())));
        balanceView.setText(String.valueOf(currency + " " + transaction.getBalance()));
        reasonView.setText(transaction.getReason());
		 
        return row;
    }
	
	@Override
	public int getCount() {
		return transactions.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	 @Override
	public Transaction getItem(int position) {
		return transactions.get(position);
	}

}
