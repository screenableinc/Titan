package classmate.screenable.titan;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetCA{
    //        check cookie validity
    Context context;
    Globals globals = new Globals();
    AccessPortal accessPortal;
    public GetCA(Context context) throws Exception{

        this.context=context;
        accessPortal=new AccessPortal(context);


    }
    public Pair<Boolean,Integer> startConnect() throws Exception {
        if (accessPortal.cookieIsValid()) {
//            proceed to page
//
            HashMap<String, String> cookies = new HashMap<>();
            cookies=accessPortal.getHashMap("cookie");
            Connection.Response ca_page = Jsoup.connect(Globals.viewca_url).cookies(cookies).method(Connection.Method.GET).userAgent(accessPortal.USER_AGENT).execute();
            Document page = ca_page.parse();

            if(accessPortal.LoginPage(page)){
//                logged out
                throw new CustomException("Error at stage one "+Globals.viewca_url);

            }else {
//                success parse data
//                Log.w(Globals.DEBUG_TAG,"SUCCESS, STARTED PARSING "+ page.toString());
                Elements tds = page.getElementsByAttributeValueStarting("class","grd");
                boolean data = getData(tds);
//                parsing success
                if (data) {
                    return new Pair<>(true, 100);
                }else {
                    throw new CustomException("Error at stage 2 "+Globals.viewca_url);
                }

            }




        } else {
//            login again


            String[] credentials = accessPortal.getcredentials();
            boolean login = accessPortal.Login(credentials[1],credentials[0],Globals.viewca_url);

            if (login){
//                    boolean data = getData()
//                get html variable containing html of target
                Document page = Jsoup.parse(accessPortal.html);
                boolean data = getData(page.getElementsByAttributeValueStarting("class","grd"));
                if (data) {
                    return new Pair<>(true, 100);
                }else {
                    throw new CustomException("Error at stage 3 "+Globals.viewca_url);
                }
            }else {
                throw new CustomException("Error at stage 4 " +Globals.viewca_url);
            }
        }

    }

    private boolean getData(Elements trs) throws Exception {


        SharedPreferences.Editor preferences = context.getSharedPreferences("setup", Context.MODE_PRIVATE).edit();
        int length=0;
        String[] keys = {"course","ass","practical","mids","ca"};
        JSONObject grades = new JSONObject();
        for (Element tr : trs) {

            Elements columns = tr.getElementsByTag("td");
            String key = columns.get(0).text();
            JSONObject innerobj = new JSONObject();
            innerobj.put(keys[1],columns.get(1).text());
            innerobj.put(keys[2],columns.get(2).text());
            innerobj.put(keys[3],columns.get(3).text());
            innerobj.put(keys[4],columns.get(4).text());
            grades.put(key,innerobj);
            length++;


        }
        if(length>0){
            preferences.putString("grades",grades.toString());
            preferences.commit();
            return true;
        }else {
            return false;
        }


    }
}

