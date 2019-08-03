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

import java.util.HashMap;

public class GetPastPapers {
    Context context;
    Globals globals = new Globals();
    AccessPortal accessPortal;
    public GetPastPapers(Context context) throws Exception{

        this.context=context;
        accessPortal=new AccessPortal(context);


    }
    public Pair<Boolean,Integer> startConnect() throws Exception {
        if (accessPortal.cookieIsValid()) {
//            proceed to page
//
            HashMap<String, String> cookies = new HashMap<>();
            cookies=accessPortal.getHashMap("cookie");
            Connection.Response ca_page = Jsoup.connect(Globals.question_bank_url).cookies(cookies).method(Connection.Method.GET).userAgent(accessPortal.USER_AGENT).execute();
            Document page = ca_page.parse();

            if(accessPortal.LoginPage(page)){
//                logged out
                throw new CustomException("Error at stage one "+Globals.question_bank_url);

            }else {
//                success parse data
//                Log.w(Globals.DEBUG_TAG,"SUCCESS, STARTED PARSING "+ page.toString());
                Elements tds = page.getElementsByAttributeValueStarting("class","grd");
                boolean data = getData(tds);
//                parsing success
                if (data) {
                    return new Pair<>(true, 100);
                }else {
                    throw new CustomException("Error at stage 2 "+Globals.question_bank_url);
                }

            }




        } else {
//            login again


            String[] credentials = accessPortal.getcredentials();
            boolean login = accessPortal.Login(credentials[1],credentials[0],Globals.question_bank_url);

            if (login){
//                    boolean data = getData()
//                get html variable containing html of target
                Document page = Jsoup.parse(accessPortal.html);
                boolean data = getData(page.getElementsByAttributeValueStarting("class","grd"));
                if (data) {
                    return new Pair<>(true, 100);
                }else {
                    throw new CustomException("Error at stage 3 "+Globals.question_bank_url);
                }
            }else {
                throw new CustomException("Error at stage 4 " +Globals.question_bank_url);
            }
        }

    }

    private boolean getData(Elements trs) throws Exception {



        int length=0;


        SQL_INTERACT interact  = new SQL_INTERACT(context);
        for (Element tr : trs) {

            Elements columns = tr.getElementsByTag("td");

            JSONObject innerobj = new JSONObject();

            String downloadLink  = columns.get(0).getElementsByAttribute("href").attr("href");
            String course = columns.get(0).getElementsByTag("a").get(0).text();
            String file_name = downloadLink.split("/",0)[downloadLink.split("/",0).length-1];

            String file_format=file_name.split(".",-1)[1];
            String id = file_name.replaceAll(" ","").replaceAll("\\.","");
            Log.w("CC",file_name+"---"+id);
            if(interact.SQLEntryExists(null,id, FeedReaderContract.FeedEntry.QB_TABLE_NAME)){
                continue;
            }

            innerobj.put("course",course);
            innerobj.put("format",file_format);
            innerobj.put("path","");
            innerobj.put("filename",file_name);
            innerobj.put("title",file_name.split("\\.",-1)[0]);
            innerobj.put("id",id);
            innerobj.put("url",downloadLink);
            innerobj.put("downloaded",false);
            innerobj.put("description","");
            interact.SQLPushUnilusPastPapers(innerobj.toString(), FeedReaderContract.FeedEntry.QB_TABLE_NAME);
            length++;


        }
        if(length>0){

            return true;
        }else {
            return false;
        }





    }

}
