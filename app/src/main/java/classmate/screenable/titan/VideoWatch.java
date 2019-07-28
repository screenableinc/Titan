package classmate.screenable.titan;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;

import java.net.URL;

public class VideoWatch extends AppCompatActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_watch);

//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
////            videoPath = bundle.getString(Const.VIDEO_PATH);
//        }else {
////            send to main acticity with toast
//        }

        final YouTubePlayerView videoView = (YouTubePlayerView) findViewById(R.id.videoView);
//        FragmentManager.FragmentLifecycleCallbacks().addObserver(youtubePlayerView);

        getLifecycle().addObserver(videoView);
        videoView.initialize(new YouTubePlayerInitListener() {
            @Override
            public void onInitSuccess(@NonNull final YouTubePlayer initializedYouTubePlayer) {
                initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady() {
                        String videoId = getIntent().getStringExtra("videoId");
                        initializedYouTubePlayer.loadVideo(videoId, 0);
                    }
                });
            }
        }, true);


//        videoView.setVideoURI(Uri.parse(uri));

    }
}
