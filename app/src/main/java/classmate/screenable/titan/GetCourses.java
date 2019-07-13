package classmate.screenable.titan;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;

public class GetCourses {
    //        check cookie validity
    Context context;
    Globals globals = new Globals();
    AccessPortal accessPortal;
    public GetCourses(Context context) throws Exception{

        this.context=context;
        accessPortal=new AccessPortal(context);


    }
    public Pair<Boolean,Integer> startConnect() throws Exception {
        if (accessPortal.cookieIsValid()) {
//            proceed to page
//
            HashMap<String, String> cookies = new HashMap<>();
            cookies=accessPortal.getHashMap("cookie");
            Connection.Response materials_page = Jsoup.connect(Globals.materials_url).cookies(cookies).method(Connection.Method.GET).userAgent(accessPortal.USER_AGENT).execute();
            Document page = materials_page.parse();

            if(accessPortal.LoginPage(page)){
//                logged out
                throw new CustomException("Error at stage 1 " +Globals.registered_courses_url);

            }else {
//                success parse data
//                Log.w(Globals.DEBUG_TAG,"SUCCESS, STARTED PARSING "+ page.toString());
                Elements options = page.getElementsByTag("option");
                boolean data = getData(options);
//                parsing success
                if (data) {
                    return new Pair<>(true, 100);
                }else {
                    throw new CustomException("Error at stage 2 " +Globals.registered_courses_url);
                }

            }




        } else {
//            login again


            String[] credentials = accessPortal.getcredentials();
            boolean login = accessPortal.Login(credentials[1],credentials[0],Globals.materials_url);

            if (login){
//                    boolean data = getData()
//                get html variable containing html of target
                Document page = Jsoup.parse(accessPortal.html);
                boolean data = getData(page.getElementsByTag("option"));
                if (data) {
                    return new Pair<>(true, 100);
                }else {
                    throw new CustomException("Error at stage 3 " +Globals.registered_courses_url);
                }
            }else {
                throw new CustomException("Error at stage 4 " +Globals.registered_courses_url);
            }
        }

    }

    private boolean getData(Elements options) throws Exception {


        SharedPreferences.Editor preferences = context.getSharedPreferences("setup", Context.MODE_PRIVATE).edit();
        int length=0;

        JSONArray courses = new JSONArray();
        for (Element tr : options) {

            String course_code = tr.attr("value");
            String course_name = tr.text();

            courses.put(course_code+"---"+course_name);
            length++;


        }
        if(length>0){
            preferences.putString("courses",courses.toString());
            preferences.commit();
            return true;
        }else {
            return false;
        }


    }
}


