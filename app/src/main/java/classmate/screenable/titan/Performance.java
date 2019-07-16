package classmate.screenable.titan;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

public class Performance extends Fragment {
    protected View rootView;
    protected String course;
    LinearLayout parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.performance, container, false);




        return rootView;
    }
    public void onFragmentEntered(){
        new Load().execute();
    }

    public class Load extends AsyncTask<String,Integer,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SharedPreferences preferences = getContext().getSharedPreferences("setup", Context.MODE_PRIVATE);
            String performance = preferences.getString("grades",null);
            String courses = preferences.getString("courses",null);
            parent=(LinearLayout) rootView.findViewById(R.id.parent);

            LoadPerformance(performance,courses);
        }

        @Override
        protected String doInBackground(String... strings) {

            return null;
        }
    }

    private void LoadPerformance(String performance, String courses){



        try{
            JSONArray array  = new JSONArray(courses);
            JSONObject grades = new JSONObject(performance);
            for(int i = 0;i<array.length();i++){
                JSONObject course = grades.getJSONObject(array.get(i).toString().split("---",-1)[0]);
                String ass = course.getString("ass");
                String mids = course.getString("mids");
                String practical = course.getString("practical");
                String ca = course.getString("ca");

                String ass_status = det_color(Float.parseFloat(ass),10f);
                String mids_status = det_color(Float.parseFloat(mids),20f);
                String practical_status = det_color(Float.parseFloat(practical),10f);
                String ca_status = det_color(Float.parseFloat(ca),30f);

                LayoutInflater inflater = LayoutInflater.from(getContext());
                LinearLayout template = (LinearLayout) inflater.inflate(R.layout.performance_template,null);

                TextView course_name = (TextView) template.findViewById(R.id.coursename);
                course_name.setText(array.get(i).toString().split("---",-1)[0]);


                TextView total = (TextView) template.findViewById(R.id.total_score);
                TextView assignment = (TextView) template.findViewById(R.id.ass_score);
                TextView mids_ = (TextView) template.findViewById(R.id.mids_score);
                TextView practical_ = (TextView) template.findViewById(R.id.practical_score);

                CircularProgressBar total_progress = (CircularProgressBar) template.findViewById(R.id.progress_total);
                CircularProgressBar mids_progress = (CircularProgressBar) template.findViewById(R.id.progress_mids);
                CircularProgressBar ass_progress = (CircularProgressBar) template.findViewById(R.id.progress_assignment);
                CircularProgressBar practical_progress = (CircularProgressBar) template.findViewById(R.id.progress_practical);

                Animate(total_progress,total,ca_status,ca,30);
                Animate(mids_progress,mids_,mids_status,mids,20);
                Animate(ass_progress,assignment,ass_status,ass,10);
                Animate(practical_progress,practical_,practical_status,practical,10);
                parent.addView(template);


            }
        }catch (Exception e){
            Log.w("TODO",e.toString());
        }
    }
    public void Animate(final CircularProgressBar bar, final TextView score, String status, String value, final int outOf){
//        turn to percentage





        final int percentage = Math.round((Float.parseFloat(value)/outOf)*100);

        ValueAnimator animation = ValueAnimator.ofInt(0,percentage);
        animation.setDuration(2000);
        Log.w("TODO","callled " +Float.parseFloat(value)+" "+status);
        if(status.equals("neutral")){
            bar.setColor(getResources().getColor(R.color.colorNeutral));
            bar.setBackgroundColor(getResources().getColor(R.color.colorNeutral));
        }else if (status.equals("good")){
            bar.setColor(getResources().getColor(R.color.colorOkay));
            bar.setBackgroundColor(getResources().getColor(R.color.colorOkay));
        }else if (status.equals("warning")){
            bar.setColor(getResources().getColor(R.color.colorWarning));
            bar.setBackgroundColor(getResources().getColor(R.color.colorWarning));
        }else if (status.equals("danger")){
            bar.setColor(getResources().getColor(R.color.colorDanger));
            bar.setBackgroundColor(getResources().getColor(R.color.colorDanger));
        }


        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();

                bar.setProgress(progress);
                String newlabel = (progress*outOf)/100.0+"";
                Log.w("TODO",newlabel+"");
                score.setText(newlabel);


            }
        });
        animation.start();
    }

    public String det_color(Float score, Float total){
//        convert score to percent
        float percent = (score/total)*100;
        if(percent>=75f){
            return "good";
        }else if(percent <75f && percent >=50f ){
            return "warning";
        }else if(percent<50f && percent >0){
            return "danger";
        }else {
            return "neutral";
        }


    }
}
