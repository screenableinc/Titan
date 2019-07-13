package classmate.screenable.titan;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class YouTube {

    public JSONObject access(String q)throws Exception{


        List<String> paramList = new ArrayList<String>();
        paramList.add("q="+ URLEncoder.encode(q,"UTF-8"));
        paramList.add("key="+ApiKeys.api_key_youtube);
        paramList.add("part=snippet");
        paramList.add("type=video");
        paramList.add("maxResults=20");
        String q_string = params(paramList);
        Log.w("TODO", q_string);

        URL Obj = new URL(Globals.api_search_url+q_string);

        HttpsURLConnection conn = (HttpsURLConnection) Obj.openConnection();
        int responseCode = conn.getResponseCode();


        BufferedReader in =
                new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();


        return new JSONObject(response.toString());
    }
    public String params(List<String> paramList){

        StringBuilder result = new StringBuilder();
        for (String param : paramList) {
            if (result.length() == 0) {
                result.append(param);
            } else {
                result.append("&" + param);
            }
        }
        return result.toString();

    }
}
