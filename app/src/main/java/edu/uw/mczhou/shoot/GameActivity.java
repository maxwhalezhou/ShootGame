package edu.uw.mczhou.shoot;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "*****GameActivity*****";

    private DrawingSurfaceView view;
    private SensorManager sensorManager;
    private GestureDetector detector;
    private Sensor sensor;
    public TextView textScore;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean gotSensor;
        super.onCreate(savedInstanceState);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Log.v(TAG, "first");
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        } else { //otherwise use the magnetometer-based one
            Log.v(TAG, "second");
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        }

        if(sensor == null) { //has no sensor
            Log.v(TAG, "No sensor available!");
            finish();
        }
        textScore = (TextView) findViewById(R.id.textScore);
        detector = new GestureDetector(this, new MyGestureListener());

        view = (DrawingSurfaceView)findViewById(R.id.drawingSurfaceView);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean gestured = detector.onTouchEvent(event);
        int action = MotionEventCompat.getActionMasked(event);

        switch(action){
            case MotionEvent.ACTION_DOWN:
                if(view.missiles.size() < 3) {
                    view.smallerPlayer.paint.setColor(Color.GREEN);
                    view.addMissile(new Missile(view.player.cx, view.player.cy)); //shoot missile
                    if (view.soundPool != null) {
                        view.soundPool.play(view.sounds[1], .2f, .2f, 1, 0, 1); //play jump sound
                    }
                } else {
                    view.smallerPlayer.paint.setColor(Color.RED);
                }
                return true;
            case MotionEvent.ACTION_UP:
                view.smallerPlayer.paint.setColor(Color.GRAY);
                return true;
            default:
                return super.onTouchEvent(event);
        }

    }



    @Override
    protected void onResume() {

        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        view.init();
        super.onResume();
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this, sensor);
        super.onPause();
    }

    //release sounds and set to null for memory
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (view.player.getLives() == 0) {
            findViewById(R.id.play_again_menu).setVisibility(View.VISIBLE);
        }

        if (textScore != null) {
            textScore.setText("" + view.points);
        }

        if(view.player.getLives() == 2) {
            findViewById(R.id.life1).setVisibility(View.INVISIBLE);
        } else if (view.player.getLives() == 1) {
            findViewById(R.id.life2).setVisibility(View.INVISIBLE);
        } else if (view.player.getLives() == 0) {
            findViewById(R.id.life3).setVisibility(View.INVISIBLE);
        }

        float[] rotationMatrix = new float[16];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

        float[] orientation = new float[3];
        SensorManager.getOrientation(rotationMatrix, orientation);

        //set bounds of player on screen and allow movement with sensor
        if(orientation[2] < -1.5) {
            view.player.setX(0 + view.player.radius);
            view.smallerPlayer.setX(0 + view.player.radius);
        } else if (orientation[2] > 1.5) {
            view.player.setX(this.getResources().getDisplayMetrics().widthPixels - view.player.radius);
            view.smallerPlayer.setX(this.getResources().getDisplayMetrics().widthPixels - view.player.radius);
        } else {
            float x = (1.0f * orientation[2]) * this.getResources().getDisplayMetrics().widthPixels
                    + this.getResources().getDisplayMetrics().widthPixels / 2.0f;
            if (x < 0 + view.player.radius) {
                view.player.setX(0 + view.player.radius);
                view.smallerPlayer.setX(0 + view.smallerPlayer.radius + view.player.radius - view.smallerPlayer.radius);
            } else if (x > this.getResources().getDisplayMetrics().widthPixels - view.player.radius) {
                view.player.setX(this.getResources().getDisplayMetrics().widthPixels - view.player.radius);
                view.smallerPlayer.setX(this.getResources().getDisplayMetrics().widthPixels - view.player.radius);
            } else {
                view.player.setX(x);
                view.smallerPlayer.setX(x);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //leave blank
    }

    public void handleMainMenu(View v) {
        findViewById(R.id.play_again_menu).setVisibility(View.INVISIBLE);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void handlePlayAgain(View v) {
        findViewById(R.id.play_again_menu).setVisibility(View.INVISIBLE);
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        view.player.setLives(3);

    }
    public class MyGestureListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onDown(MotionEvent e) {
            return true; //we got this
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            Log.v(TAG, "Fling!!");
            if (view.player.powerUp == 1) {
                view.points += view.circles.size() * 50;  //add all circles to points
                view.circles.clear(); //remove all circles on screen
                view.player.setPowerUp(0); //remove powerup status
            }

            return true; //we handled this
        }
    }


}
