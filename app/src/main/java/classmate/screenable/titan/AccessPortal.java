package classmate.screenable.titan;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

public class AccessPortal{
    Context context;
    public Globals globals = new Globals();
    HashMap<String, String> cookies = new HashMap<>();
    HashMap<String, String> formData = new HashMap<>();
//    check if user has already been redirected to login page
    boolean redirected=false;

//    pass the html to this global param
    String html =null;
    final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36";
    public AccessPortal(Context context){
        this.context=context;
    }

    public boolean Login(String s_id, String password, String target_url )throws Exception{

        String loginFormUrl = globals.login_url;
        String loginActionUrl = globals.login_url;

//        get page content
        Connection.Response loginForm = Jsoup.connect(loginFormUrl).method(Connection.Method.GET).userAgent(USER_AGENT).execute();
        Document loginDoc = loginForm.parse();

        cookies.putAll(loginForm.cookies());

        Element eventValidation = loginDoc.select("input[name=__EVENTVALIDATION]").first();
        Element viewState = loginDoc.select("input[name=__VIEWSTATE]").first();
        Element viewStategen = loginDoc.select("input[name=__VIEWSTATEGENERATOR]").first();

        formData.put("__VIEWSTATE",viewState.attr("value"));
        formData.put("__VIEWSTATEGENERATOR",viewStategen.attr("value"));
        formData.put("__EVENTVALIDATION",eventValidation.attr("value"));
        formData.put("ctl00$MainContent$User","Student");
        formData.put("ctl00$MainContent$UserName",s_id);
        formData.put("ctl00$MainContent$Password",password);
        formData.put("ctl00$MainContent$Button1","Log In");

//        send post
        Connection.Response login = Jsoup.connect(loginActionUrl)
                .cookies(cookies)
                .data(formData)
                .method(Connection.Method.POST)
                .userAgent(USER_AGENT)
                .execute();
        Log.w("TODOd",login.statusCode()+"");
        Connection.Response homePage = Jsoup.connect(target_url)
                .cookies(cookies)
                .method(Connection.Method.GET)
                .userAgent(USER_AGENT)
                .execute();
        Document portal = homePage.parse();
        if(portal.toString().contains("Oooops!!!!!")){
            Toast.makeText(context,"Portal conjested",Toast.LENGTH_LONG).show();
            return false;
        }
        if(!LoginPage(portal)){
//            successful login in store new cookie hash map and time
//            store user name and password too
            SharedPreferences.Editor editor = context.getSharedPreferences("credentials",Context.MODE_PRIVATE).edit();
            editor.putString(globals.id_keyName,s_id.toUpperCase());
            editor.putString(globals.passwordKeyName,password);
            editor.putBoolean("loggedin",true);
            saveHashMap("cookie",cookies);
            html=portal.toString();
            editor.commit();
            return true;


        }else {
            return false;
        }

    }

    public boolean LoginPage(Document document){
        if(document.getElementById("MainContent_Button1")==null){
            return false;
        }else {
            return true;
        }

    }


//    public String[][] getRegFormDetails() throws Exception{
//        checkConnect();
//
//        return null;
//    }


    private boolean getData(Elements tds){
        for (Element td:tds){
            Log.w("HTMLtds",tds.val());
        }
        return false;
    }


    public String[] getcredentials(){
        SharedPreferences preferences = context.getSharedPreferences("credentials",Context.MODE_PRIVATE);
        String password = preferences.getString(globals.passwordKeyName,"");
        String student_id = preferences.getString(globals.id_keyName,"");

        String[] credentials = {password,student_id};
        return credentials;
    }

    public boolean cookieIsValid(){
        SharedPreferences preferences = context.getSharedPreferences("cookie",Context.MODE_PRIVATE);
        long lastUseTime = preferences.getLong("lastuse",0);
        if(lastUseTime==0){
            return false;
        }else {
//            compare time
            Timestamp current = new Timestamp(new Date().getTime());
            Timestamp lastuse = new Timestamp(lastUseTime);
            long difference = compareTwoTimeStamps(current,lastuse);
            if(difference>15){
                return false;
            }else {
                return true;
            }

        }

    }
    public long compareTwoTimeStamps(java.sql.Timestamp currentTime, java.sql.Timestamp oldTime)
    {
        long milliseconds1 = oldTime.getTime();
        long milliseconds2 = currentTime.getTime();

        long diff = milliseconds2 - milliseconds1;
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);

        return diffMinutes;
    }

    public void saveHashMap(String key , Object obj) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        editor.putString(key,json);
        editor.apply();     // This line is IMPORTANT !!!

//        save cookie use time
//        Timestamp ts = new Timestamp(new Date().getTime());
        SharedPreferences.Editor preferences = context.getSharedPreferences("cookie",Context.MODE_PRIVATE).edit();
        preferences.putLong("lastuse",new Date().getTime());
        preferences.apply();


    }

    public HashMap<String,String> getHashMap(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
        Gson gson = new Gson();
        String json = prefs.getString(key,"");
        java.lang.reflect.Type type = new TypeToken<HashMap<String,String>>(){}.getType();
        HashMap<String,String> obj = gson.fromJson(json, type);
        cookies=obj;
        return obj;
    }
}