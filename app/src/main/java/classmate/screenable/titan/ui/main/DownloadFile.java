package classmate.screenable.titan.ui.main;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadFile extends AsyncTask {
    String url;
    String filename;
    public DownloadFile(String url,String filename){
        this.url=url;
        this.filename=filename;
    }

    @Override
    protected Object doInBackground(Object[] objects){
        try {
            URLConnection conn = new URL(url).openConnection();

            int lengthOfFile = conn.getContentLength();
            BufferedInputStream in = new BufferedInputStream(conn.getInputStream(),8192);

        }catch (MalformedURLException e){

        }catch (IOException e){

        }
        return null;
    }
}
