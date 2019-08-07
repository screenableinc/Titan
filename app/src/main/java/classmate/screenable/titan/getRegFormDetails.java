package classmate.screenable.titan;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class getRegFormDetails{
    //        check cookie validity
    Context context;
    Globals globals = new Globals();
    AccessPortal accessPortal;
    public getRegFormDetails(Context context) throws Exception{

        this.context=context;
        accessPortal=new AccessPortal(context);


    }
    public Pair<Boolean,Integer> startConnect() throws Exception {
        if (accessPortal.cookieIsValid()) {
//            proceed to page
//
            HashMap<String, String> cookies = new HashMap<>();
            cookies=accessPortal.getHashMap("cookie");
            Connection.Response registration = Jsoup.connect(Globals.registration_url).cookies(cookies).method(Connection.Method.GET).userAgent(accessPortal.USER_AGENT).execute();
            Document page = registration.parse();

            if(accessPortal.LoginPage(page)){
//                logged out
                throw new CustomException("Error at stage one");

            }else {
//                success parse data
//                Log.w(Globals.DEBUG_TAG,"SUCCESS, STARTED PARSING "+ page.toString());
                Elements tds = page.getElementsByTag("td");
                boolean data = getData(tds);
//                parsing success
                if (data) {
                    return new Pair<>(true, 100);
                }else {
                    throw new CustomException("Error at stage two");
                }

            }




        } else {
//            login again
            Log.w(Globals.DEBUG_TAG,"COOKIEINVALID");

            String[] credentials = accessPortal.getcredentials();
            boolean login = accessPortal.Login(credentials[1],credentials[0],Globals.registration_url);

            if (login){
//                    boolean data = getData()
//                get html variable containing html of target
                Document page = Jsoup.parse(accessPortal.html);
                boolean data = getData(page.getElementsByTag("td"));
                if (data) {
                    return new Pair<>(true, 100);
                }else {
                    throw new CustomException("Error at stage 3");
                }
            }else {
                throw new CustomException("Error at stage 4");
            }
        }

    }

    private boolean getData(Elements tds){
        List<String[]> data = new ArrayList<>();
        String[] keys = {"program of study","mode of study","year", "semester"};
        boolean program_gotten=false;
        boolean mode_gotten=false;
        boolean year_gotten=false;
        boolean semester_gotten=false;
        SharedPreferences.Editor preferences = context.getSharedPreferences("setup",Context.MODE_PRIVATE).edit();

        for (Element td:tds){
            if(td.text().toLowerCase().contains(keys[0])){
                String program=deEmptyCharacterfy(td.text().split(":",-1)[1]);

                preferences.putString("program",program);program_gotten=true;

            }else if(td.text().toLowerCase().contains(keys[1])){
                String mode =deEmptyCharacterfy(td.text().split(":",-1)[1]).toLowerCase();
                if(mode.contains("full")){
                    mode="fulltime";
                }else if(mode.contains("part")){
                    mode="parttime";
                }else if (mode.contains("dist")){
                    mode="distance";
                }

                preferences.putString("mode", mode);mode_gotten=true;

            }else if(td.text().toLowerCase().contains(keys[2])){
                String string = td.text().replace("YEAR OF STUDY:","").replace("SEMESTER:","").replaceAll(" ","");
                String year =deEmptyCharacterfy(string.charAt(0)+"");
                String sem =deEmptyCharacterfy(string.charAt(1)+"");

                preferences.putString("year",year);
                preferences.putString("semester",sem);
                preferences.putString("level","undergraduate");
                year_gotten=true;
                semester_gotten=true;


            }
        }
        Log.w("TODO",year_gotten+" "+semester_gotten+" "+program_gotten+" "+mode_gotten);
        if(program_gotten&&semester_gotten&&mode_gotten&&year_gotten){
            preferences.commit();
            return true;
        }else {
            return false;
        }

    }
    private String deEmptyCharacterfy(String specimen){
        while (specimen.startsWith(" ")){
            specimen=specimen.substring(1);
        }
        while (specimen.endsWith(" ")){
            specimen=specimen.substring(0,specimen.length()-1);
        }


        return specimen;
    }
}
