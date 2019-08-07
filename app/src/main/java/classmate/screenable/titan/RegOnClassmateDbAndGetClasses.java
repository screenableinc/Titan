package classmate.screenable.titan;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class RegOnClassmateDbAndGetClasses {
    final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36";

    public Pair<Boolean,Integer> access(Context context) throws Exception{
//        buld queryString list in youtube class
        SharedPreferences preferences = context.getSharedPreferences("setup",Context.MODE_PRIVATE);
        SharedPreferences credentials = context.getSharedPreferences("credentials",Context.MODE_PRIVATE);
        String mode =  preferences.getString("mode",null);
        String student_id =  credentials.getString(Globals.id_keyName,null);
        String level =  preferences.getString("level",null);
        String year =  preferences.getString("year",null);
        String semester =  preferences.getString("semester",null);
        String courses =  preferences.getString("courses",null);
        JSONArray courses_object = new JSONArray(courses);
        JSONArray array = new JSONArray();
        for (int i=0;i<courses_object.length();i++){
            String code = courses_object.getString(i).split("---",-1)[0];
            array.put(code);
        }

        String program =  preferences.getString("program",null);
        List<String> params=new ArrayList<>();
        params.add("mode="+mode);
        params.add("student_id="+student_id);
        params.add("level="+level);
        params.add("year="+year);
        params.add("semester="+semester);
        params.add("courses="+ URLEncoder.encode(array.toString(),"UTF-8"));
        params.add("program="+URLEncoder.encode(program,"UTF-8"));

        String qString = qStringGen.qString(params);

        URL Obj = new URL(Globals.api_classmate_url+qString);


        HttpURLConnection conn = (HttpURLConnection) Obj.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent",USER_AGENT);
        conn.setRequestProperty("Accept","*/*");

        int responseCode = conn.getResponseCode();


        BufferedReader in =
                new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        Log.w("CLASSMATEAPI",response.toString());
        JSONObject object = new JSONObject(response.toString());

        if(object.getBoolean("success")){
            SharedPreferences.Editor editor = context.getSharedPreferences("setup",Context.MODE_PRIVATE).edit();
            editor.putString("timetable",object.getString("data"));
            editor.putString("free_classes",object.getString("free_classes"));
            editor.commit();

            return new Pair<>(true,100);
        }else {
            throw new CustomException("failed at classmate api");

        }

    }

}
