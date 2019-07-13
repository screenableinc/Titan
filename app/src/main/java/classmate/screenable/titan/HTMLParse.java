package classmate.screenable.titan;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class HTMLParse {
    Document document;
    public HTMLParse(Document document){
        this.document = document;
    }
    public JSONArray GetRegCourses(){
        Elements elements = Jsoup.parse(document.toString()).getElementById("divPrint").getElementsByTag("tr");
        for (org.jsoup.nodes.Element element:elements){
            Log.w("HTML",element.toString());
        }
    return null;
    }
}
//package classmate.screenable.titan;
//
//        import android.content.Context;
//        import android.util.Log;
//
//        import org.jsoup.Connection;
//        import org.jsoup.Jsoup;
//        import org.jsoup.nodes.Document;
//        import org.jsoup.nodes.Element;
//        import org.jsoup.select.Elements;
//
//public class getRegFormDetails{
//    //        check cookie validity
//    Context context;
//    Globals globals = new Globals();
//    AccessPortal accessPortal;
//    private getRegFormDetails(Context context) throws Exception{
//        checkConnect();
//        this.context=context;
//        accessPortal=new AccessPortal(context);
//
//    }
//    private boolean checkConnect() throws Exception {
//        if (accessPortal.cookieIsValid()) {
////            proceed to page
////                TODO::add cookies
//            Connection.Response loginForm = Jsoup.connect(globals.registration_url).method(Connection.Method.GET).userAgent(accessPortal.USER_AGENT).execute();
//            org.jsoup.nodes.Document loginDoc = loginForm.parse();
//            Elements tds = loginDoc.getElementsByTag("td");
//            boolean data = getData(tds);
//
//        } else {
////            login again
//
//            String[] credentials = accessPortal.getcredentials();
//            boolean login = accessPortal.Login(credentials[1],credentials[0],globals.registration_url);
//
//            if (login){
////                    boolean data = getData()
//            }
//        }
//        return false;
//    }
//
//    private boolean getData(Elements tds){
//        for (Element td:tds){
//            Log.w("HTMLtds",tds.val());
//        }
//        return false;
//    }
//}
