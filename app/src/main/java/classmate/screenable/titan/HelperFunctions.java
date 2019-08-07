package classmate.screenable.titan;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import classmate.screenable.titan.BuildConfig;

import static com.google.common.net.HttpHeaders.USER_AGENT;

public class HelperFunctions {


    public static class UpdateClasses extends AsyncTask {
        Context context;
        LinearLayout sync_button;

        public UpdateClasses(Context context, LinearLayout sync) {
            this.context = context;
            this.sync_button=sync;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sync_button.setVisibility(View.GONE);
            Toast.makeText(context, "Updating classes", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            SharedPreferences preferences = context.getSharedPreferences("setup",Context.MODE_PRIVATE);
            SharedPreferences credentials = context.getSharedPreferences("credentials",Context.MODE_PRIVATE);
            String mode =  preferences.getString("mode",null);
            String program =  preferences.getString("program",null);
            String level =  preferences.getString("level",null);
            String year =  preferences.getString("year",null);
            String semester =  preferences.getString("semester",null);
            List<String> params=new ArrayList<>();
            params.add("mode="+mode);

            params.add("level="+level);
            params.add("year="+year);
            params.add("semester="+semester);

            try {

                params.add("program=" + URLEncoder.encode(program, "UTF-8"));
                String qString = qStringGen.qString(params);

                URL Obj = new URL(Globals.update_classes_url + qString);


                HttpURLConnection conn = (HttpURLConnection) Obj.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", Globals.USER_AGENT);
                conn.setRequestProperty("Accept", "*/*");
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);
                conn.connect();
                int responseCode = conn.getResponseCode();


                BufferedReader in =
                        new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject object = new JSONObject(response.toString());

                if (object.getBoolean("success")) {
                    Log.w("UPDATE",object.toString());
                    SharedPreferences.Editor editor = context.getSharedPreferences("setup", Context.MODE_PRIVATE).edit();
                    editor.putString("timetable", object.getString("data"));
                    editor.putString("free_classes", object.getString("free_classes"));
                    editor.commit();

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Done",Toast.LENGTH_LONG).show();

                        }
                    });


                } else {

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Update failed",Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Update failed",Toast.LENGTH_LONG).show();

                    }
                });
//
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            sync_button.setVisibility(View.VISIBLE);
        }
    }



    public static class CheckVersion extends AsyncTask{
        Context context;
        public CheckVersion(Context context){
            this.context=context;
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            try {

                URL url = new URL(Globals.version_check_url);


                URLConnection connection = url.openConnection();
//            connection.setConnectTimeout(5000);
                connection.setDefaultUseCaches(false);
                connection.setUseCaches(false);
                connection.setConnectTimeout(3000);
                connection.setReadTimeout(3000);
                connection.connect();



                InputStream response = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(response);
                BufferedReader reader1 = new BufferedReader(reader);
                StringBuilder _result = new StringBuilder();
                String line;
                while ((line = reader1.readLine()) != null) {
                    _result.append(line);
                }
                final String result = _result.toString();
                final JSONObject object = new JSONObject(result);
                final String[] _fixes=object.getJSONObject("result").getString("fixes").split(";",0);
                final String version=object.getJSONObject("result").getString("mobile_app_version");
                if(object.getInt("status")==200){
//                    query successful check versions
                    if(!version.equals(Globals.APP_VERSION_NUMBER)){
//                        prompt download
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                String fixes_text="";
                                for (int i = 0; i < _fixes.length; i++) {
                                    fixes_text = fixes_text + "\n" + _fixes[i];
                                }
                                new AlertDialog.Builder(context).setTitle("Update to version "+version)
                                        .setMessage(fixes_text)
                                        .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();

                                                Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(Globals.APP_DOWNLOAD_LINK));
                                                context.startActivity(openBrowser);
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        }).create().show();






                            }
                        });
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }


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
