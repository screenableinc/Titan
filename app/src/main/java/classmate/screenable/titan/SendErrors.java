package classmate.screenable.titan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import classmate.screenable.titan.CustomException;
import classmate.screenable.titan.Globals;

public class SendErrors  extends AsyncTask<String, Integer, String> {
    Context context;
    public SendErrors(Context context){
        this.context=context;


    }
    public void start(){

    }


    @Override
    protected String doInBackground(String... strings) {
        SharedPreferences credentials = context.getSharedPreferences("credentials", Context.MODE_PRIVATE);
        String errors = context.getSharedPreferences("errors", Context.MODE_PRIVATE).getString("errors", "[]");
        String student_id = credentials.getString(Globals.id_keyName, null);
        if (errors.equals("[]")){
            Log.w("TODO_ironic","no errors");
            return null;
        }

        Log.w("TODO_ironic","should have started");

        try {
            JSONArray array = new JSONArray(errors);
            List<String> params = new ArrayList<>();
            params.add("student_id=" + student_id);
            params.add("errors=" + URLEncoder.encode(array.toString(), "UTF-8"));
            String qString = qStringGen.qString(params);
            URL Obj = new URL(Globals.api_error_report + qString);

            HttpURLConnection conn = (HttpURLConnection) Obj.openConnection();
            int responseCode = conn.getResponseCode();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.connect();


            BufferedReader in =
                    new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject object = new JSONObject(response.toString());

            if (object.getBoolean("success")) {
                SharedPreferences.Editor editor = context.getSharedPreferences("errors", Context.MODE_PRIVATE).edit();
                editor.putString("errors", "[]");
                editor.commit();


            } else {



            }
            return null;
        }catch (Exception e){

            Log.w("TODO_ironic",e.toString());
            return null;
        }
    }


}



