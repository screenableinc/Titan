package classmate.screenable.titan;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static android.content.ContentValues.TAG;
import static classmate.screenable.titan.Globals.folder;
import static classmate.screenable.titan.Globals.slideshare_folder;
import static classmate.screenable.titan.Globals.unilus_folder;

public class DownloadFile extends AsyncTask {
    String _url;
    String filename;
    Context context;
    String id;
    CircularProgressBar progressBar;
    ProgressBar loading;
    ImageView downloadIcon;
    ImageView checked;
    String source;
    public DownloadFile(String id, String filename, Context context, CircularProgressBar progressBar, ProgressBar loading, ImageView downloadIcon, String source, String url, ImageView checked){
        this.id=id;
        this.filename=filename;
        this.progressBar = progressBar;
        this.context = context;
        this.checked = checked;
        this.loading=loading;
        this.source=source;
        this.downloadIcon=downloadIcon;
        this._url=url;

    }

    @Override
    protected Object doInBackground(Object[] objects){
        int count;


        try {
            //        get downloadlink
            String downloadLink;
            if(source.equals("slideshare")){
                downloadLink = new SlideShare().getSlideShow(this.id);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        loading.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);

                    }
                });
            }else {
                downloadLink=Globals.material_prefix+_url.replace("..","");
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        loading.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);

                    }
                });

            }


            URL url =new URL(downloadLink);
            URLConnection conn = url.openConnection();
            conn.connect();
            String format = conn.getContentType().split("/",-1)[1];

            filename = filename.replace("."+format,"."+format);
            if(!filename.toLowerCase().contains("."+format)){
//                filename=filename;
                filename=filename+"."+format;
            }

            int lengthOfFile = conn.getContentLength();
            InputStream input = new BufferedInputStream(url.openStream(),8192);
//            if not file.exists
            File directory;
            String path;
            if(source.equals("slideshare")){
             directory= new File(slideshare_folder);
             path=slideshare_folder+ File.separator+filename;
            }else {
                directory=new File(unilus_folder);
                path=unilus_folder+ File.separator+filename;
            }

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
                publishProgress("" + (int) ((total * 100) / lengthOfFile));
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
            if(source.equals("slideshare")){
                table= FeedReaderContract.FeedEntry.TABLE_NAME;
            }else {
                table= FeedReaderContract.FeedEntry.UNILUS_DOC_TABLE_NAME;
            }
            new SQL_INTERACT(context).setPath(id,path,table);

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    checked.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            });

            return true;


        }catch (Exception e){
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    downloadIcon.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(context,"Download failed",Toast.LENGTH_LONG).show();
                }
            });
            Log.w("DOWNLOADTASK",e.toString());
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Object[] progress) {
        super.onProgressUpdate(progress);

        progressBar.setProgress(Integer.parseInt(progress[0].toString()));

    }
}
