package classmate.screenable.titan;

import android.content.Context;
import android.content.res.Resources;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Classes {
    Context context;
    public Classes(Context context){
        this.context=context;
    }
    public JSONObject getFreeClasses()throws Exception {

//        TODO::: get free classes from server too
        Resources resources = context.getResources();
        InputStreamReader read = new InputStreamReader(resources.openRawResource(R.raw.free_classes));
        BufferedReader reader = new BufferedReader(read);
        String string_json = reader.readLine();
        JSONObject json_of_free_classes = new JSONObject(string_json);
        return json_of_free_classes;
    }
    public JSONObject getClasses()throws Exception {

        Resources resources = context.getResources();
        InputStreamReader read = new InputStreamReader(resources.openRawResource(R.raw.programs_scheds));
        BufferedReader reader = new BufferedReader(read);
        String string_json = reader.readLine();
        JSONObject classes = new JSONObject(string_json);
        return classes;

    }
}
