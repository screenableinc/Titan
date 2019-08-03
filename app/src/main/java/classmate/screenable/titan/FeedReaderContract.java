package classmate.screenable.titan;

import android.provider.BaseColumns;

public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "SAVED_DOCUMENTS";
        public static final String ASS_TABLE_NAME = "ASSIGNMENTS";
        public static final String QB_TABLE_NAME = "QUESTIONBANK";
        public static final String UNILUS_DOC_TABLE_NAME = "UNILUS_SAVED_DOCUMENTS";
        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_TITLE = "TITLE";
        public static final String COLUMN_NAME_THUMBNAILURL = "THUMB_URL";
        public static final String COLUMN_NAME_FORMAT = "FORMAT";
        public static final String COLUMN_NAME_DOWNLOADABLE = "DOWNLOADABLE";
        public static final String COLUMN_NAME_DOWNLOADED = "DOWNLOADED";
        public static final String COLUMN_NAME_PATH = "PATH";
        public static final String COLUMN_NAME_URL = "URL";

        public static final String COLUMN_NAME_COURSE = "COURSE";
        public static final String COLUMN_NAME_DESCR = "DESCRIPTION";
        public static final String COLUMN_NAME_TYPE = "TYPE";
        public static final String COLUMN_NAME_FILENAME = "TYPE";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry.COLUMN_NAME_ID + " TEXT PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_TITLE + " TEXT," +
                    FeedEntry.COLUMN_NAME_FORMAT + " TEXT," +
                    FeedEntry.COLUMN_NAME_DOWNLOADABLE + " TEXT," +
                    FeedEntry.COLUMN_NAME_DOWNLOADED + " TEXT," +
                    FeedEntry.COLUMN_NAME_PATH + " TEXT," +
                    FeedEntry.COLUMN_NAME_COURSE + " TEXT," +
                    FeedEntry.COLUMN_NAME_URL + " TEXT," +
                    FeedEntry.COLUMN_NAME_TYPE + " TEXT," +
                    FeedEntry.COLUMN_NAME_THUMBNAILURL + " TEXT)";

    public static final String SQL_QUESTIONBANK_TABLE =
            "CREATE TABLE " + FeedEntry.QB_TABLE_NAME + " (" +
                    FeedEntry.COLUMN_NAME_ID + " TEXT PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_TITLE + " TEXT," +
                    FeedEntry.COLUMN_NAME_FORMAT + " TEXT," +
                    FeedEntry.COLUMN_NAME_COURSE + " TEXT," +

                    FeedEntry.COLUMN_NAME_DOWNLOADED + " TEXT," +
                    FeedEntry.COLUMN_NAME_PATH + " TEXT," +
                    FeedEntry.COLUMN_NAME_URL + " TEXT," +

                    FeedEntry.COLUMN_NAME_THUMBNAILURL + " TEXT)";


    public static final String SQL_CREATE_ENTRIES_UNILUS_DOC =
            "CREATE TABLE " + FeedEntry.UNILUS_DOC_TABLE_NAME + " (" +
                    FeedEntry.COLUMN_NAME_ID + " TEXT PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_TITLE + " TEXT," +
                    FeedEntry.COLUMN_NAME_FORMAT + " TEXT," +
                    FeedEntry.COLUMN_NAME_DESCR + " TEXT," +
                    FeedEntry.COLUMN_NAME_DOWNLOADED + " TEXT," +
                    FeedEntry.COLUMN_NAME_PATH + " TEXT," +
                    FeedEntry.COLUMN_NAME_URL + " TEXT," +
                    FeedEntry.COLUMN_NAME_FILENAME + " TEXT," +

                    FeedEntry.COLUMN_NAME_COURSE + " TEXT)";


    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
    public static final String SQL_DELETE_UNILUS_DOC_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.UNILUS_DOC_TABLE_NAME;
    public static final String SQL_DELETE_QB_TABLE =
            "DROP TABLE IF EXISTS " + FeedEntry.UNILUS_DOC_TABLE_NAME;
}
