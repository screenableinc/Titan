package classmate.screenable.titan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import static android.content.ContentValues.TAG;
import static classmate.screenable.titan.Globals.slideshare_folder;
import static classmate.screenable.titan.Globals.unilus_folder;

public class DownloadProfileImage {
    Context context;
    public DownloadProfileImage(Context context){
        this.context=context;


    }
    public boolean download( final ImageView view){

        SharedPreferences preferences = context.getSharedPreferences("credentials",Context.MODE_PRIVATE);
        String student_id =  preferences.getString(Globals.id_keyName,null);

        int count;


        try {
            //        get downloadlink
            String downloadLink;



            URL url =new URL(Globals.portal_pic+student_id+".jpg");
            URLConnection conn = url.openConnection();
            conn.connect();


//             = filename.replace("."+format,"."+format);
//            if(!filename.toLowerCase().contains("."+format)){
////                filename=filename;
//                filename=filename+"."+format;
//            }

            int lengthOfFile = conn.getContentLength();
            InputStream input = new BufferedInputStream(url.openStream(),8192);
//            if not file.exists
            File directory = new File(Globals.profile_folder);
            final String path=directory+File.separator+student_id+".jpg";

            if (!directory.exists()) {
                directory.mkdirs();
            }
            // Output stream to write file

            OutputStream output = new FileOutputStream(path);
            Log.w("DOWNLOAD",path);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called

                Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));


                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();
            String table;



            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Picasso.get().load(path).into(view);
//                    Toast.makeText(context,"failed getting profile picture",Toast.LENGTH_LONG).show();
                }
            });

            return true;


        }catch (Exception e){
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_person_black_24dp));

                    Toast.makeText(context,"failed getting profile picture",Toast.LENGTH_LONG).show();
                }
            });
            Log.w("DOWNLOADTASK",e.toString());
        }

        return true;
    }
}
