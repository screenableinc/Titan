package classmate.screenable.titan;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class GetCoursesFromPreference {
    Context context;

    public GetCoursesFromPreference(Context context){
        this.context=context;

    }

    public Pair<List<String>,List<String>> codeAndName() throws Exception{
        List<String> codes = new ArrayList<>();
        List<String> names = new ArrayList<>();
        SharedPreferences preferences = context.getSharedPreferences("setup",Context.MODE_PRIVATE);
        String string_courses=preferences.getString("courses",null);
        if(string_courses==null){
            return null;
        }else {
            JSONArray coursesArray = new JSONArray(string_courses);
            for (int i = 0; i < coursesArray.length(); i++) {
                String code = coursesArray.getString(i).split("---",-1)[0];
                String name = coursesArray.getString(i).split("---",-1)[1];
                codes.add(code);names.add(name);


            }
            return new Pair<>(codes,names);
        }

    }
}
