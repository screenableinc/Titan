package classmate.screenable.titan;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ErrorRecorder {
    public ErrorRecorder(Context context, Exception exception, String severity){
        String errorclass = exception.getStackTrace()[0].getClassName();
        String errormethod = exception.getStackTrace()[0].getMethodName();
//        Load into errors pref
        SharedPreferences errors = context.getSharedPreferences("errors",Context.MODE_PRIVATE);
        String errorsArray=errors.getString("errors","[]");
        SharedPreferences.Editor editor = context.getSharedPreferences("errors",Context.MODE_PRIVATE).edit();

        try {
        long unixTime = System.currentTimeMillis() / 1000L;

        JSONArray array = new JSONArray(errorsArray);
        JSONObject object = new JSONObject();
        object.put("class",errorclass);
        object.put("method",errormethod);
        object.put("message", exception.toString());
        object.put("timestamp", unixTime);
        object.put("severity", severity);
        array.put(object);
        editor.putString("errors",array.toString());
        editor.commit();


        }catch (Exception e){
            Log.w("TODO_ironic",e.toString());
        }



    }
}
