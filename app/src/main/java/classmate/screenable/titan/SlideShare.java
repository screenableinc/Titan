package classmate.screenable.titan;

import android.util.Log;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import org.json.JSONObject;
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
     public JSONObject access(String q) throws Exception{
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

        Log.w("SLIDESHARE",response.toString());
//
//            Log.w("TODO",response.toString());
//            return new JSONObject(response.toString());
         return null;
    }
}
