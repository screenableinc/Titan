package classmate.screenable.titan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static classmate.screenable.titan.FeedReaderContract.SQL_CREATE_ENTRIES;
import static classmate.screenable.titan.FeedReaderContract.SQL_CREATE_ENTRIES_UNILUS_DOC;
import static classmate.screenable.titan.FeedReaderContract.SQL_DELETE_ENTRIES;
import static classmate.screenable.titan.FeedReaderContract.SQL_DELETE_QB_TABLE;
import static classmate.screenable.titan.FeedReaderContract.SQL_DELETE_UNILUS_DOC_ENTRIES;
import static classmate.screenable.titan.FeedReaderContract.SQL_QUESTIONBANK_TABLE;

public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "classmate.db";

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES); db.execSQL(SQL_CREATE_ENTRIES_UNILUS_DOC);db.execSQL(SQL_QUESTIONBANK_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_UNILUS_DOC_ENTRIES);
        db.execSQL(SQL_DELETE_QB_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}