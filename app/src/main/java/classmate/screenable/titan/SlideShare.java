package classmate.screenable.titan;

import android.util.Log;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import org.json.JSONArray;
import org.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.EncryptedPrivateKeyInfo;
import javax.net.ssl.HttpsURLConnection;

public class SlideShare  {
     public JSONArray access(String q) throws Exception{
         List<String> paramList = new ArrayList<String>();
         long unixTime = System.currentTimeMillis() / 1000L;
         final HashCode hashCode = Hashing.sha1().hashString(ApiKeys.shared_secret_slideshare+unixTime, Charset.defaultCharset());


         paramList.add("q="+ URLEncoder.encode(q,"UTF-8"));
        paramList.add("api_key="+ApiKeys.api_key_slideshare);
        paramList.add("ts="+unixTime);
        paramList.add("hash="+hashCode);
        paramList.add("maxResults=20");
        String q_string = new YouTube().params(paramList);
        Log.w("TODO", q_string);

         URL Obj = new URL(Globals.api_slideshare_url+q_string);

         HttpsURLConnection conn = (HttpsURLConnection) Obj.openConnection();
         int responseCode = conn.getResponseCode();

//         todo at futsal read xml
         BufferedReader in =
                 new BufferedReader(new InputStreamReader(conn.getInputStream()));
         String inputLine;
         StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
             response.append(inputLine);
         }
            in.close();
         Document doc = Jsoup.parse(response.toString(), "",Parser.xmlParser());
           Elements slideshows=doc.select("SlideShow");
           JSONArray data = new JSONArray();
         for (int i = 0; i < slideshows.size(); i++) {
             JSONObject object = new JSONObject();
             object.put("id",slideshows.get(i).select("ID").text());
             object.put("title",slideshows.get(i).select("Title").text());
             object.put("thumbnailurl",slideshows.get(i).select("ThumbnailURL").text());
             object.put("format",slideshows.get(i).select("format").text());
             object.put("downloadable",slideshows.get(i).select("Download").text());
             object.put("url",slideshows.get(i).select("DownloadUrl").text());
             object.put("slideshowtype",slideshows.get(i).select("SlideshowType").text());
             data.put(object);
         }

        Log.w("SLIDESHARE",data.toString()+"");
//
//            Log.w("TODO",response.toString());
//            return new JSONObject(response.toString());
         return data;
    }

    private String getValue(){

         return null;
    }
}
