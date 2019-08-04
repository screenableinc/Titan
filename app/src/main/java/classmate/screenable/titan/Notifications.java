package classmate.screenable.titan;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

/**
 * Created by Wise on 10/24/2017.
 */

public class Notifications {
    Context context;
    String message;
    String title;
    AudioManager mobilemode;
    int currentvol;

    public void Notifications(Context context, String message, String title) {
        this.context = context;
        this.title = title;
        this.message = message;
        show();

    }
    public void show(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification_cm)
                        .setContentTitle(title)
                        .setContentText(message);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);
        mobilemode = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());

        currentvol = mobilemode.getStreamVolume(AudioManager.STREAM_RING);
        mobilemode.setStreamVolume(AudioManager.STREAM_RING,mobilemode.getStreamMaxVolume(AudioManager.STREAM_RING),0);

        new ReturnVolumeToNormal().execute();





    }

public class ReturnVolumeToNormal extends AsyncTask<String, Integer, String> {
    @Override
    protected String doInBackground(String... params) {
        try {
            Ringtone r  = RingtoneManager.getRingtone(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

//            AudioManager mobilemode = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

            r.play();

            Thread.sleep(3000);
            r.stop();
//            int
            mobilemode.setStreamVolume(AudioManager.STREAM_RING,currentvol,0);


        }catch (Exception e){
            Log.w("CC","something went wooooo");
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
//        int currentvol = mobilemode.getStreamVolume(AudioManager.STREAM_RING);
//        mobilemode.setStreamVolume(AudioManager.STREAM_RING,currentvol,0);

//        Toast.makeText(context,"done",Toast.LENGTH_LONG).show();

    }
}


}
