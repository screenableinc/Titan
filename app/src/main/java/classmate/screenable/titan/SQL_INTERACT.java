package classmate.screenable.titan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SQL_INTERACT {
    Context context;
    public SQL_INTERACT(Context context){
        this.context=context;
    }

    public boolean SQLPushUnilusDocs(String JObject) throws Exception{

        JSONObject object = new JSONObject(JObject);
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ID,object.getString("id"));
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,object.getString("title"));
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_COURSE,object.getString("course"));
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DESCR,object.getString("description"));
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FORMAT,object.getString("format"));
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DOWNLOADED,object.getBoolean("downloaded")+"");
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PATH,object.getString("path"));
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_URL,object.getString("url"));
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FILENAME,object.getString("filename"));

        long newRowId = db.insert(FeedReaderContract.FeedEntry.UNILUS_DOC_TABLE_NAME, null, values);
        if(newRowId==-1){
            throw new CustomException("Error on log material into db unilus");
        }else {
            return true;

        }





    }

    public boolean SQLPush(String JObject) throws Exception{

        JSONObject object = new JSONObject(JObject);
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ID,object.getString("id"));
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,object.getString("title"));
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_THUMBNAILURL,object.getString("thumbnailurl"));
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FORMAT,object.getString("format"));
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DOWNLOADABLE,object.getString("downloadable"));
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DOWNLOADED,object.getBoolean("downloaded")+"");
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PATH,object.getString("path"));
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_URL,object.getString("url"));
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TYPE,object.getString("slideshowtype"));

        long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        if(newRowId==-1){
            return false;
        }else {
            return true;
        }





    }



    public boolean SQLEntryExists(String [] projection, String id, String table){
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = {id};

        Cursor cursor = db.query(
                table,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null              // The sort order
        );
        List itemIds = new ArrayList<>();
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_ID
                    ));
            itemIds.add(itemId);
        }
        cursor.close();
        if(!itemIds.isEmpty()){
            return true;
        }else {
            return false;
        }







    }

    public JSONArray GetDocuments(String table) throws Exception{
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_ID + " = ?";


        Cursor cursor = db.rawQuery("select * from "+ table,null);
        List itemIds = new ArrayList<>();
        JSONArray array = new JSONArray();
        while(cursor.moveToNext()) {
            JSONObject jsonObject = new JSONObject();
            if(table.equals(FeedReaderContract.FeedEntry.TABLE_NAME)) {
                String itemId = cursor.getString(
                        cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_ID
                        ));
                String title = cursor.getString(
                        cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE
                        ));
                String path = cursor.getString(
                        cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_PATH
                        ));
                String thumb_url = cursor.getString(
//                    use in case file deleted
                        cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_THUMBNAILURL
                        ));
                String downloadable = cursor.getString(
                        cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_DOWNLOADABLE
                        ));
                String downloaded = cursor.getString(
                        cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_DOWNLOADED
                        ));

                jsonObject.put("id", itemId);
                jsonObject.put("title", title);
                jsonObject.put("path", path);
                jsonObject.put("downloadable", downloadable);
                jsonObject.put("thumbnailurl", thumb_url);
                jsonObject.put("downloaded", downloaded);
            }else if(table.equals(FeedReaderContract.FeedEntry.UNILUS_DOC_TABLE_NAME)){
                String itemId = cursor.getString(
                        cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_ID
                        ));
                String title = cursor.getString(
                        cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_FILENAME
                        ));
                String path = cursor.getString(
                        cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_PATH
                        ));
                String format = cursor.getString(
//                    use in case file deleted
                        cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_FORMAT
                        ));
                String url = cursor.getString(
                        cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_URL
                        ));
                String downloaded = cursor.getString(
                        cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_DOWNLOADED
                        ));

                jsonObject.put("id", itemId);
                jsonObject.put("title", title);
                jsonObject.put("path", path);
                jsonObject.put("url", url);
                jsonObject.put("format", format);
                jsonObject.put("downloaded", downloaded);
            }
            array.put(jsonObject);
        }
        cursor.close();


    return array;



    }
    public boolean setPath(String id, String path) throws Exception{
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PATH,path);
        long newRowId =  db.update(FeedReaderContract.FeedEntry.UNILUS_DOC_TABLE_NAME, values, FeedReaderContract.FeedEntry.COLUMN_NAME_ID+"='"+id+"'",null);

        if(newRowId==-1){
            throw new CustomException("Error on log material into db unilus");
        }else {
            return true;

        }
    }
}
