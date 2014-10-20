package sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	private static MySQLiteHelper instance = null;
	public static final String TABLE_TRANSACTIONS = "transactions";
	public static final String COLUMN_ID = "_id";
  	public static final String COLUMN_FILEID = "fileID";
  	public static final String COLUMN_REASON = "reason";
  	public static final String COLUMN_TIME = "time";
  	public static final String COLUMN_WITHDRAWAL = "withdrawal";
  	public static final String COLUMN_WOLTLABID = "woltlabID";
  	public static final String COLUMN_BALANCE = "balance";

  	private static final String DATABASE_NAME = "transactions.db";
  	private static final int DATABASE_VERSION = 1;

  	// Database creation sql statement
  	private static final String DATABASE_CREATE = "CREATE TABLE "
      + TABLE_TRANSACTIONS + "(" + 
      COLUMN_ID + " INTEGER PRIMARY KEY, " + 
      COLUMN_FILEID + " INTEGER NOT NULL, " +
      COLUMN_REASON + " TEXT NOT NULL, " +
      COLUMN_TIME + " INTEGER NOT NULL, " +
      COLUMN_WITHDRAWAL + " INTEGER NOT NULL, " +
      COLUMN_WOLTLABID + " INTEGER NOT NULL, " +
      COLUMN_BALANCE + " REAL NOT NULL);";

  	private MySQLiteHelper(Context context) {
  		super(context, DATABASE_NAME, null, DATABASE_VERSION);
  	}
  
	public static MySQLiteHelper getInstance(Context ctx) {
	    if (instance == null) {
	    	instance = new MySQLiteHelper(ctx.getApplicationContext());
	    }
	    return instance;
	}

  	@Override
  	public void onCreate(SQLiteDatabase database) {
	  database.execSQL(DATABASE_CREATE);
  	}

  	@Override
  	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	  Log.w(MySQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    	db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
    	onCreate(db);
  	}

}