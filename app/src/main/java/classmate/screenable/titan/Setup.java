package classmate.screenable.titan;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.Set;


public class Setup extends AppCompatActivity {

    CircularProgressBar circularProgressBar;
    TextView stage;
    TextView progess_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        circularProgressBar=(CircularProgressBar) findViewById(R.id.progress_load);
        stage= (TextView) findViewById(R.id.stage);
        progess_= (TextView) findViewById(R.id.progress);
        new StartSetup().execute();


    }

    public void Animate(Integer start,Integer end){
        ValueAnimator animation = ValueAnimator.ofInt(start,end);
        animation.setDuration(200);

        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                circularProgressBar.setProgress(progress);
                String newlabel = progress+"%";;
                Log.w("TODO","callled");
                progess_.setText(newlabel);


            }
        });
        animation.start();
    }

    private class StartSetup extends AsyncTask<String,Integer,String>{
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            String[] stages = {"Getting details","Getting courses","Getting CA","Getting Lecturers details", "Finalising"};

            switch (values[0]){
                case 1:
                    Animate(0,25);

                    stage.setText(stages[0]);
                case 2:
                    Animate(25,50);
                    stage.setText(stages[1]);

                case 3:
                    Animate(50,75);
                    stage.setText(stages[2]);
                case 4:
                    Animate(75,100);
                    stage.setText(stages[3]);

            }
        }

        @Override
        protected String doInBackground(String... strings) {
//            stage one -- registration details
            try {
//                Pair<Boolean,Integer> registration =  new getRegFormDetails(getApplicationContext()).startConnect();
//                publishProgress(1);
//
//
//                Pair<Boolean,Integer> courses =  new GetCourses(getApplicationContext()).startConnect();
//                publishProgress(2);
//                Pair<Boolean,Integer> ca =  new GetCA(getApplicationContext()).startConnect();
//                publishProgress(3);
                Pair<Boolean,Integer> reg_on_screenable =  new RegOnClassmateDbAndGetClasses().access(getApplicationContext());
//                Pair<Boolean,Integer> contact =  new GetLecturerContacts(getApplicationContext()).startConnect();
//                publishProgress(4);
//                no error proceed to main page
                startActivity(new Intent(Setup.this,MainActivity.class));
                finish();
            }catch (Exception e){
                Log.w("SETUP_TODO",e.toString());
            }

            return null;
        }
    }
}
