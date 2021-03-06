package sql;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import data.Transaction;

public class TransactionsDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_FILEID,
			MySQLiteHelper.COLUMN_REASON, MySQLiteHelper.COLUMN_TIME, MySQLiteHelper.COLUMN_WITHDRAWAL,
			MySQLiteHelper.COLUMN_WOLTLABID, MySQLiteHelper.COLUMN_BALANCE };

	public TransactionsDataSource(Context context) {
		dbHelper = MySQLiteHelper.getInstance(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void beginTransaction() {
		database.beginTransaction();
	}

	public void setTransactionSuccessful() {
		database.setTransactionSuccessful();
	}

	public void endTransaction() {
		database.endTransaction();
	}

	public Transaction create(int transactionID, int fileID, String reason, int time, int withdrawal, int woltlabID,
			float balance) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_ID, transactionID);
		values.put(MySQLiteHelper.COLUMN_FILEID, fileID);
		values.put(MySQLiteHelper.COLUMN_REASON, reason);
		values.put(MySQLiteHelper.COLUMN_TIME, time);
		values.put(MySQLiteHelper.COLUMN_WITHDRAWAL, withdrawal);
		values.put(MySQLiteHelper.COLUMN_WOLTLABID, woltlabID);
		values.put(MySQLiteHelper.COLUMN_BALANCE, balance);

		long insertId = database.insert(MySQLiteHelper.TABLE_TRANSACTIONS, null, values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_TRANSACTIONS, allColumns, MySQLiteHelper.COLUMN_ID + " = "
				+ insertId, null, null, null, null);
		cursor.moveToFirst();

		Transaction transaction = cursorToTransaction(cursor);
		cursor.close();
		return transaction;
	}

	public void delete(int transactionID) {
		System.out.println("Transaction deleted with id: " + transactionID);
		database.delete(MySQLiteHelper.TABLE_TRANSACTIONS, MySQLiteHelper.COLUMN_ID + " = " + transactionID, null);
	}

	public List<Transaction> getList() {
		List<Transaction> transactions = new ArrayList<Transaction>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_TRANSACTIONS, allColumns, null, null, null, null,
				MySQLiteHelper.COLUMN_ID + " DESC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Transaction transaction = cursorToTransaction(cursor);
			transactions.add(transaction);
			cursor.moveToNext();
		}

		// make sure to close the cursor
		cursor.close();
		return transactions;
	}

	public Transaction getLastTransaction() {
		Cursor cursor = database.query(MySQLiteHelper.TABLE_TRANSACTIONS, allColumns, null, null, null, null,
				MySQLiteHelper.COLUMN_ID + " DESC", "1");

		if (!cursor.moveToFirst()) {
			Transaction emptyTransaction = new Transaction();
			return emptyTransaction;
		}

		return cursorToTransaction(cursor);
	}

	private Transaction cursorToTransaction(Cursor cursor) {
		Transaction transaction = new Transaction();

		transaction.setTransactionID(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_ID)));
		transaction.setFileID(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_FILEID)));
		transaction.setReason(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_REASON)));
		transaction.setTime(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_TIME)));
		transaction.setWithdrawal(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_WITHDRAWAL)));
		transaction.setWoltlabID(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_WOLTLABID)));
		transaction.setBalance(cursor.getFloat(cursor.getColumnIndex(MySQLiteHelper.COLUMN_BALANCE)));

		return transaction;
	}
}
