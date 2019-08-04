package classmate.screenable.titan;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Wise on 10/24/2017.
 */

public class Reminders extends Service {


        AlarmReceiver alarm = new AlarmReceiver();
        public void onCreate()
        {
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId)
        {

            alarm.setAlarm(this);
            return START_STICKY;
        }

        @Override
        public void onStart(Intent intent, int startId)
        {
            Log.w("CC","llllllllllllll");
            alarm.setAlarm(this);
        }

        @Override
        public IBinder onBind(Intent intent)
        {
            return null;
        }


//
// /**
//         * A constructor is required, and must call the super IntentService(String)
//         * constructor with a name for the worker thread.
//         */
//        public Reminders() {
//            super("Reminders");
//        }
//
//        /**
//         * The IntentService calls this method from the default worker thread with
//         * the intent that started the service. When this method returns, IntentService
//         * stops the service, as appropriate.
//         */
//        @Override
//        protected void onHandleIntent(Intent intent) {
//            // Normally we would do some work here, like download a file.
//            // For our sample, we just sleep for 5 seconds.
////            Toast.makeText(getApplicationContext(),"try this",Toast.LENGTH_LONG).show();
//            sendBroadcast(new Intent(Reminders.this,AlarmReceiver.class));
////            AlarmManager manager = AlarmManager.OnAlarmListener
//        }
}
