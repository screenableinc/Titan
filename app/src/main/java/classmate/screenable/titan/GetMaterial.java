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

import static com.google.common.net.HttpHeaders.USER_AGENT;

public class GetMaterial {
    Context context;
    Globals globals = new Globals();
    AccessPortal accessPortal;
    String course;
    public GetMaterial(Context context, String course) throws Exception{

        this.context=context;
        accessPortal=new AccessPortal(context);
        this.course=course;

    }

    public Pair<Boolean,Integer> startConnect() throws Exception {
        if (accessPortal.cookieIsValid()) {

//            check if i can access materials page
//            if not...login
//            access page
//            get form data
//            post
//            proceed to page
//
            HashMap<String, String> cookies = new HashMap<>();
            cookies=accessPortal.getHashMap("cookie");
            Connection.Response materials_page = Jsoup.connect(Globals.materials_url).cookies(cookies).method(Connection.Method.GET).userAgent(accessPortal.USER_AGENT).execute();
            Document page = materials_page.parse();



            if(accessPortal.LoginPage(page)){
//                logged out
                throw new CustomException("Error at stage one "+Globals.materials_url);

            }else {

                boolean post = post(page,accessPortal.getHashMap("cookie"));
                if(post){
                    return new Pair<>(true,100);

                }else {
                    throw new CustomException("Error at " );
                }

//                success parse data and get form details
//                Log.w(Globals.DEBUG_TAG,"SUCCESS, STARTED PARSING "+ page.toString());

//                ctl00$ctl00$MainContent$MainContent$Course: ECF350
            }




        } else {
//            login again


            String[] credentials = accessPortal.getcredentials();
            boolean login = accessPortal.Login(credentials[1],credentials[0],Globals.materials_url);

            if (login){
//                    boolean data = getData()
//                get html variable containing html of target
                Document page = Jsoup.parse(accessPortal.html);
                boolean post = post(page,accessPortal.getHashMap("cookie"));
                if (post) {
                    return new Pair<>(true,100);
                }else {
                    throw new CustomException("Error at stage 3 "+Globals.materials_url);
                }
            }else {
                throw new CustomException("Error at stage 4 " +Globals.materials_url);
            }
        }

    }

    private boolean post(Document page, HashMap<String, String> cookies) throws Exception{
        HashMap<String, String> formData = new HashMap<>();
        String  eventValidation = page.select("input[name=__EVENTVALIDATION]").first().attr("value");
        String viewState = page.select("input[name=__VIEWSTATE]").first().attr("value");
        String viewStategen = page.select("input[name=__VIEWSTATEGENERATOR]").first().attr("value");
        String eventTarget = page.select("input[name=__EVENTTARGET]").first().attr("value");

        formData.put("__EVENTVALIDATION",eventValidation);
        formData.put("__VIEWSTATE",viewState);
        formData.put("__VIEWSTATEGENERATOR",viewStategen);
        formData.put("__EVENTTARGET",eventTarget);
        formData.put("ctl00$ctl00$MainContent$MainContent$Course",course);
        formData.put("ctl00$ctl00$MainContent$MainContent$Button1","Search");

        try {

            Connection.Response material_post = Jsoup.connect(Globals.materials_url)
                    .cookies(cookies)
                    .data(formData)
                    .method(Connection.Method.POST)
                    .userAgent(USER_AGENT)
                    .execute();
            Log.w("CC","reached here punk");
            Document document  = material_post.parse();
            if(!accessPortal.LoginPage(document)){
                Elements elements =document.getElementsByAttributeValueStarting("class","grd");
                if(getData(elements)){
                    return true;
                }else {
                    throw new CustomException("Error at stage 8");
                }

            }else {
//                parse data and load to db
                throw new CustomException("Error at stage 7 ");
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new CustomException("Error at stage 9 "+e.toString());
        }


    }
    private boolean parseData(Document document){

        Elements trs = document.getElementsByAttributeValueStarting("class","grd");
        for (Element tr : trs) {
            Elements columns = tr.getElementsByTag("td");

        }


        return false;
    }
    private boolean getData(Elements trs) throws Exception {



        int length=0;

        SQL_INTERACT interact  = new SQL_INTERACT(context);
        for (Element tr : trs) {

            Elements columns = tr.getElementsByTag("td");
            String key = columns.get(0).text();
            JSONObject innerobj = new JSONObject();
            String id = columns.get(4).text().replaceAll(" ","").replaceAll("/","").replaceAll(":","");
            if(interact.SQLEntryExists(null,id, FeedReaderContract.FeedEntry.UNILUS_DOC_TABLE_NAME)){
                continue;
            }

            String downloadLink  = columns.get(2).getElementsByAttribute("href").attr("href");
            String course = columns.get(2).getElementsByTag("a").get(0).text();

            innerobj.put("url",downloadLink);
            String file_name = downloadLink.split("/",0)[downloadLink.split("/",0).length-1];
            String file_format=file_name.split(".",-1)[1];
            innerobj.put("course", course);
            innerobj.put("format", file_format);
            innerobj.put("path", "");
            innerobj.put("filename", file_name);
            innerobj.put("downloaded",false);
            innerobj.put("title",file_name.split(".",-1)[0]);
            innerobj.put("description",columns.get(3).text());

            innerobj.put("id",id);
            interact.SQLPushUnilusDocs(innerobj.toString());

            length++;


        }
        if(length>0){

            return true;
        }else {
            return false;
        }


    }

}
