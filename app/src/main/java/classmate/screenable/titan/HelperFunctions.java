package classmate.screenable.titan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import classmate.screenable.titan.BuildConfig;

public class HelperFunctions {








    public static void openFile(String path, Context context){
        Intent myIntent = new Intent(Intent.ACTION_VIEW);
        File file = new File(path);
        myIntent.setData(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider",file));
        Intent j = Intent.createChooser(myIntent, "Choose an application to open with:");
        context.startActivity(j);

    }
    public static boolean permissions(Context context){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted...request now
            return false;


        }else {
            return true;
        }

    }
    private static boolean deleteFile(String path) throws Exception{
        File dir = new File(Globals.folder);
        File file=new File(dir,path);
        Log.w("filedelete",path+"lol");
        file.delete();
        return true;
    }
    public static class deleteFromDbAndStorage extends AsyncTask {
        String table;
        String id;
        String column;
        String path;
        boolean success;
        View view;
        View parent;
        Context context;

        public deleteFromDbAndStorage(String table, String id, String column,String path, View parent, View view, Context context){
            this.table=table;this.view = view;this.parent=parent;
            this.id=id;
            this.path=path;
            this.context = context;
            this.column=column;
        }

        @Override
        protected void onPostExecute(Object o) {
            if(success){
                ((LinearLayout) parent).removeView(view);
                Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Object doInBackground(Object[] objects){

            try {
                boolean deleteFromdb = new SQL_INTERACT(context).DeleteEntry(id,table,column);
                boolean delFile=deleteFile(path);
                //this part is dangerously iffy
                if (deleteFromdb || delFile) {
                    Log.w("CC",path+"PPPP");
                    success=true;
//                hanlde response
                }
            }catch (Exception e){
                e.printStackTrace();
            }


            return null;
        }
    }
}
