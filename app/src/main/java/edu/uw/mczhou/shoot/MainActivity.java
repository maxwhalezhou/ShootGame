package edu.uw.mczhou.shoot;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements Runnable {
    private MediaPlayer player;
    private BackgroundSound mBackgroundSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBackgroundSound = new BackgroundSound();
        run();
        player = MediaPlayer.create(this, R.raw.background_first_steps);
        player.start();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.start();
            }
        });

    }

    @Override
    public void run() {
        mBackgroundSound.execute(this);
    }

    @Override
    protected void onResume() {
//        player = MediaPlayer.create(this, R.raw.background_first_steps);
//        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                player.start();
//            }
//        });
//        if(player != null) {
//            player.start();
//        }

        super.onResume();
    }

    @Override
    protected void onPause() {
//        if(player != null) {
//            player.stop();
//        }
//        mBackgroundSound.cancel(true);
        super.onPause();
    }

    @Override
    protected void onStop() {
//        if(player != null) {
//            player.stop();
//            player.release();
//            player = null;
//        }
        super.onStop();
    }

    public static final String ACTION_SMS_SENT = "edu.uw.mczhou.yama.ACTION_PLAY_GAME";

    public void playGame(View v) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public class BackgroundSound extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            Log.v("BACKGROUND", "PLAYING");
            MediaPlayer player = MediaPlayer.create(MainActivity.this, R.raw.background_first_steps);
            player.setLooping(true);
            player.setVolume(1.0f, 1.0f);
            player.start();

            return null;
        }

    }
}
