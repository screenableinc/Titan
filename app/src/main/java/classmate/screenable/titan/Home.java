package classmate.screenable.titan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
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
    LinearLayout classesLayout;
    SharedPreferences preferences;
    String courses;
    Integer[] backgrounds;
    String classes;
    String selected_day;
    String selected_time;

    int dayofweek = rightNow.get(Calendar.DAY_OF_WEEK);
    String[] strDays = new String[] { "Sunday", "Monday", "Tuesday",
            "Wednesday", "Thursday", "Friday", "Saturday" };
    String day = strDays[dayofweek-1];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.home, container, false);
        freeclasseslayout = (LinearLayout) rootView.findViewById(R.id.llfreeclasses);
        courseslayout = (LinearLayout) rootView.findViewById(R.id.llcourses);
        classesLayout = (LinearLayout) rootView.findViewById(R.id.llclasses);
        preferences = getContext().getSharedPreferences("setup", Context.MODE_PRIVATE);
        courses = preferences.getString("courses",null);
        classes = preferences.getString("timetable",null);
        Integer [] _backgrounds = {R.drawable.bg,R.drawable.bg2,R.drawable.bg3,R.drawable.bg4,R.drawable.bg5};
        backgrounds=_backgrounds;



        if(courses==null){
            Log.w("TODO","error with courses");
        }else {
            LoadCourses(courses);
        }
        if(classes==null){
//            TODO::::: handle this error
            Log.w("TODO","should have why");

        }else {
            LoadClasses(classes,dayofweek);
        }
//        load free classes function if time is between 8 and 20
//        LoadFreeClasses();
        String time;
        if(currentHour>=20){
            currentHour=20;
        }else if (currentHour<8){
            currentHour=20;
        }
        if(currentHour>=17){
            time = currentHour+":30";
        }else {
            time = currentHour+":00";
        }



//        DAYSPNNER_____________________
        Spinner day_spinner = (Spinner) rootView.findViewById(R.id.whatday);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> days_adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.daysofweek, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        days_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        day_spinner.setAdapter(days_adapter);
        day_spinner.setSelection(dayofweek-1);

        day_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(((TextView) parent.getChildAt(0))!=null) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorAccent));
                    ((TextView) parent.getChildAt(0)).setTextSize(14);
                }
                selected_day=strDays[position];
                freeclasseslayout.removeAllViews();
                LoadFreeClasses(selected_day,selected_time);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//__________________TIME SPINNER___________________
        final String[] spinner_times = {time,"08:00","09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:30","17:30","18:30","19:30","20:30"};
        final Spinner atwhattime = (Spinner) rootView.findViewById(R.id.atwhattime);
        final ArrayAdapter<String> time_adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,spinner_times);
        time_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        atwhattime.setAdapter(time_adapter);
        atwhattime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(((TextView) parent.getChildAt(0))!=null) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorAccent));
                    ((TextView) parent.getChildAt(0)).setTextSize(14);

                }
                selected_time=spinner_times[position];
                freeclasseslayout.removeAllViews();
                LoadFreeClasses(selected_day,selected_time);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//        spinner
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.daysofweek, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setSelection(dayofweek-1);


//        set value of spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(((TextView) parent.getChildAt(0))!=null) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorAccent));
                    ((TextView) parent.getChildAt(0)).setTextSize(14);
                }
//
                classesLayout.removeAllViews();


                LoadClasses(classes,position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return rootView;


    }
    public void LoadClasses(String classes,int the_day){
        try{
            JSONArray classes_on_day = new JSONObject(classes).getJSONArray(strDays[the_day]);
            for (int i = 0; i <classes_on_day.length() ; i++) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.nextclass,null);
                FrameLayout background = (FrameLayout) layout.findViewById(R.id.frame);
                int randomNum = ThreadLocalRandom.current().nextInt(0, 5);
                background.setBackgroundResource(backgrounds[randomNum]);
//
                JSONObject _class = new JSONObject(classes_on_day.get(i).toString());
                final String lecturer = _class.getString("lecturer");
//                TODO:::NOte program is the name of the course
                final String course = _class.getString("program");
                final String code = _class.getString("code");
                String room = _class.getString("room");
                final String time = _class.getString("time");
                final String type = _class.getString("type");
//                for display
                char firstletter = code.charAt(0);
                String starting = time.split("-",-1)[0];
                final String room_only=room.split("\\(",-1)[0];
                String campus="";
                if(room.toLowerCase().contains("main")){
                    campus="Main";
                }else {
                    campus="L.H";
                }
                final String campus_=campus;
                TextView _room=(TextView) layout.findViewById(R.id.room);
                TextView _time = (TextView) layout.findViewById(R.id.time);
                _room.setText("Room "+room_only);
                _time.setText("@"+starting);
                TextView startswith = (TextView) layout.findViewById(R.id.startswith);
                startswith.setText(firstletter+"");
                TextView footer=(TextView) layout.findViewById(R.id.footer);
                footer.setText(code.toString());
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder;
                        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
                        LinearLayout class_details_dialog=(LinearLayout) layoutInflater.inflate(R.layout.class_details_dialog,null);
                        TextView course_code=(TextView) class_details_dialog.findViewById(R.id.course_code);
                        Log.w("TODO",course_code.toString());
                        course_code.setText(code);
                        TextView course_name=(TextView) class_details_dialog.findViewById(R.id.coursename);
                        course_name.setText(course);
                        TextView room=(TextView) class_details_dialog.findViewById(R.id.room);
                        room.setText(room_only);
                        TextView _campus=(TextView) class_details_dialog.findViewById(R.id.campus);
                        _campus.setText(campus_);
                        TextView _type=(TextView) class_details_dialog.findViewById(R.id.type);
                        _type.setText(type);

                        TextView duration=(TextView) class_details_dialog.findViewById(R.id.duration);
                        duration.setText(time);
                        TextView instructor=(TextView) class_details_dialog.findViewById(R.id.instructor);
                        instructor.setText(lecturer);
                        builder = new AlertDialog.Builder(getContext());

                        builder.setView(class_details_dialog).show();



                    }
                });

                classesLayout.addView(layout);
                Log.w("TODO","should have");

            }



        }catch (Exception e){
            Log.w("TODOd",e.toString());

        }
    }
    public void LoadCourses(String courses){


        try {

            JSONArray mycourses = new JSONArray(courses);

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
    public void LoadFreeClasses(String what_day, String what_time){

        try {
            JSONObject freeclasses = new Classes(getContext()).getFreeClasses();


//            final String final_hr = time;
            JSONObject free_classes_on_day = freeclasses.getJSONObject(what_day);

            JSONArray classes = free_classes_on_day.getJSONArray(what_time);
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