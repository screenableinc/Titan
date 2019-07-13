package classmate.screenable.titan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

public class Home extends Fragment {
    protected View rootView;
    protected String course;
    Calendar rightNow = Calendar.getInstance();
    int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
    LinearLayout freeclasseslayout;
    LinearLayout courseslayout;

    int dayofweek = rightNow.get(Calendar.DAY_OF_WEEK);
    String[] strDays = new String[] { "Sunday", "Monday", "Tuesday",
            "Wednesday", "Thursday", "Friday", "Saturday" };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.home, container, false);
        freeclasseslayout = (LinearLayout) rootView.findViewById(R.id.llfreeclasses);
        courseslayout = (LinearLayout) rootView.findViewById(R.id.llcourses);
        SharedPreferences preferences = getContext().getSharedPreferences("setup", Context.MODE_PRIVATE);
        String courses = preferences.getString("courses",null);

        if(courses==null){
            Log.w("TODO","error with courses");
        }else {
            LoadCourses(courses);
        }
//        load free classes function if time is between 8 and 20
        LoadFreeClasses();
        return rootView;


    }
    public void LoadClasses(){

    }
    public void LoadCourses(String courses){


        try {

            JSONArray mycourses = new JSONArray(courses);
            int[] backgrounds = {R.drawable.bg,R.drawable.bg2,R.drawable.bg3,R.drawable.bg4,R.drawable.bg5};
            for (int i=0;i<mycourses.length();i++){
//                randomise color
                LayoutInflater inflater = LayoutInflater.from(getContext());
                LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.class_room_template,null);
                FrameLayout background = (FrameLayout) layout.findViewById(R.id.frame);
                int randomNum = ThreadLocalRandom.current().nextInt(0, 5);
                background.setBackgroundResource(backgrounds[randomNum]);
//                campus on footer, room in text
                String code = mycourses.get(i).toString().split("---",-1)[0];
                char firstletter = code.charAt(0);


                TextView textView = (TextView) layout.findViewById(R.id.insidetext);
                textView.setText(firstletter+"");
                TextView footer = (TextView) layout.findViewById(R.id.footer);

                footer.setText(code);
                courseslayout.addView(layout);



            }
        }catch (Exception e){
            Log.w("LOADING",e.toString());

        }
    }

    public void LoadNextClass(){

    }
    public void LoadFreeClasses(){
//        TODO come back and fix late night classes
        try {
            JSONObject freeclasses = new Classes(getContext()).getFreeClasses();
            String day = strDays[dayofweek-1];
            String time;
            if(currentHour>=17){
                time = currentHour+":30";
            }else {
                time = currentHour+":00";
            }
            JSONObject free_classes_on_day = freeclasses.getJSONObject(day);

            JSONArray classes = free_classes_on_day.getJSONArray(time);
            int[] backgrounds = {R.drawable.bg,R.drawable.bg2,R.drawable.bg3,R.drawable.bg4,R.drawable.bg5};
            for (int i=0;i<classes.length();i++){
//                randomise color
                LayoutInflater inflater = LayoutInflater.from(getContext());
                LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.class_room_template,null);
                FrameLayout background = (FrameLayout) layout.findViewById(R.id.frame);
                int randomNum = ThreadLocalRandom.current().nextInt(0, 5);
                background.setBackgroundResource(backgrounds[randomNum]);
//                campus on footer, room in text
                String room = classes.getString(i).split("\\(",-1)[0];
                String campus;
                if(classes.getString(i).toLowerCase().contains("main")){
                    campus = "Main";
                }else {
                    campus="L.H";
                }

                TextView textView = (TextView) layout.findViewById(R.id.insidetext);
                textView.setText(room);
                TextView footer = (TextView) layout.findViewById(R.id.footer);

                footer.setText(campus);
                freeclasseslayout.addView(layout);


            }
        }catch (Exception e){
            Log.w("LOADING",e.toString());

        }
        }
}