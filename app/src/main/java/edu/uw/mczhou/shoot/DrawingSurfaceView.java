package edu.uw.mczhou.shoot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Shader;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.HashSet;
import java.util.Iterator;
import android.os.Vibrator;
import android.widget.ImageView;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * An SurfaceView for generating graphics on. Controls the various shapes drawn
 * on screen.
 * @author Maxwell Zhou
 */
public class DrawingSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "SurfaceView";
    public long playTime;

    private int viewWidth, viewHeight; //size of the view

    private Bitmap bmp; //image to draw on
    public int points;
    private  SurfaceHolder mHolder; //the holder we're going to post updates to
    private DrawingRunnable mRunnable; //the code that we'll want to run on a background thread
    private Thread mThread; //the background thread
    public CopyOnWriteArraySet<Circle> circles; //store all circles
    public SoundPool soundPool;
    public CopyOnWriteArraySet<Missile> missiles; //store all missiles
    private Random r;
    private CopyOnWriteArraySet<Star> stars;
    private HashSet<Star> toRemoveStars;
    public Player player;
    public Player smallerPlayer;
    private PowerUp powerUp;
    private Vibrator v;
    public int[] sounds;
    public boolean[] isLoaded;
    public HashSet<Circle> toRemoveCircles;

    public DrawingSurfaceView(Context context) {
        this(context, null);
    }

    public DrawingSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawingSurfaceView(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);

        viewWidth = 1; viewHeight = 1; //positive defaults; will be replaced when #surfaceChanged() is called

        // register our interest in hearing about changes to our surface
        mHolder = getHolder();
        mHolder.addCallback(this);

        mRunnable = new DrawingRunnable();
        init();
        initializeSoundPool();
    }

    //initialize all variables
    public void init() {
        r = new Random();
        playTime = 0;
        points = 0;
        circles = new CopyOnWriteArraySet<>();
        missiles = new CopyOnWriteArraySet<>();
        toRemoveCircles = new HashSet<>();
        stars = new CopyOnWriteArraySet<>();
        toRemoveStars = new HashSet<>();
        v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        player = new Player(getContext().getResources().getDisplayMetrics().widthPixels / 2,
                getContext().getResources().getDisplayMetrics().heightPixels * .9f);
        smallerPlayer = new Player(getContext().getResources().getDisplayMetrics().widthPixels / 2,
                getContext().getResources().getDisplayMetrics().heightPixels * .9f);
        smallerPlayer.radius = 5f;
        smallerPlayer.paint.setColor(Color.GRAY);
        smallerPlayer.cy -= player.radius / 2;
    }

    //helper method for setting up the sound pool
    @SuppressWarnings("deprecation")
    private void initializeSoundPool(){
        //TODO: Create the SoundPool
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes.Builder audioBuilder = new AudioAttributes.Builder();
            audioBuilder.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
            audioBuilder.setUsage(AudioAttributes.USAGE_GAME);
            AudioAttributes attributes = audioBuilder.build();

            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(5);
            builder.setAudioAttributes(attributes);

            soundPool = builder.build();
        } else {
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }

        sounds = new int[2];
        isLoaded = new boolean[2];

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    Log.v(TAG, "Sound " + sampleId + " loaded.");
                    sounds[sampleId - 1] = sampleId;
                    isLoaded[sampleId - 1] = true;
                }
            }
        });

        soundPool.load(getContext(), R.raw.eject, 1); //load click
        soundPool.load(getContext(), R.raw.jump, 1); //load jump
    }

    //adds a new Missile to the list
    public synchronized void addMissile(Missile add) {
        missiles.add(add);
    }

    /**
     * Helper method for the "game loop"
     */
    public synchronized void update(){
        //update the "game state" here (move things around, etc.
        //TODO: fill in your own logic here!


        //check if lifes
        //if no lives, create dialog and stop

        HashSet<Missile> toRemoveMissiles = new HashSet<>(); //missile set to be removed at end

        playTime++; //increment playtime

        //add stars to screen
        if (playTime % 15 == 0) {
            stars.add(new Star(r.nextInt(getContext().getResources().getDisplayMetrics().widthPixels), 0));
        }

        //more circles on screen as points go up
        if (points < 750) {
            if (playTime % 150 == 0) {
                addCircle();
            }
        } else if (points >= 750 && points <= 1400) {
            if (playTime % 110 == 0) {
                addCircle();
            }
        } else if (points >= 1400 && points <= 3000) {
            if (playTime % 90 == 0) {
                addCircle();
            }
        } else if (points >= 3000 && points <= 5000) {
            if (playTime % 80 == 0) {
                addCircle();
            }
        } else if (points >= 5000 && points <= 8000) {
            if (playTime % 70 == 0) {
                addCircle();
            }
        }  else if (points >= 5000 && points <= 8000) {
            if (playTime % 65 == 0) {
                addCircle();
            }
        }  else if (points >= 8000 && points <= 12000) {
            if (playTime % 60 == 0) {
                addCircle();
            }
        }  else if (points >= 12000 && points <= 16000) {
            if (playTime % 52 == 0) {
                addCircle();
            }
        }  else if (points >= 16000 && points <= 19000) {
            if (playTime % 47 == 0) {
                addCircle();
            }
        }  else if (points >= 19000 && points <= 23000) {
            if (playTime % 42 == 0) {
                addCircle();
            }
        }  else if (points >= 23000 && points <= 27000) {
            if (playTime % 35 == 0) {
                addCircle();
            }
        } else {
            if (playTime % 30 == 0) {
                addCircle();
            }
        }

        if(playTime % 500 == 0) { //occasionally add powerup
            powerUp = new PowerUp(r.nextInt(getContext().getResources().getDisplayMetrics().widthPixels), 0);
        }

        //move powerup on Screen
        if(powerUp != null) {
            powerUp.y += powerUp.dy;

        }

        //move circles on screen
        for(Circle circle: circles) {
            circle.cy += circle.dy * 1.4;
        }

        //move stars
        for(Star star: stars) {
            star.y += star.dy;
            if(star.y > getContext().getResources().getDisplayMetrics().heightPixels) {
                toRemoveStars.add(star);
            }
        }

        Iterator<Missile> missileIterator = missiles.iterator();
        while(missileIterator.hasNext()) {
            Missile nextMissile = missileIterator.next();
            nextMissile.y -= 9;

            //removes missiles that go off screen
            if(nextMissile.y < 5) {
                toRemoveMissiles.add(nextMissile);
            }

            Iterator<Circle> circleIterator = circles.iterator();
            while(circleIterator.hasNext()) {
                Circle nextCircle = circleIterator.next();

                //removes circles that go off screen
                if(nextCircle.cy > getContext().getResources().getDisplayMetrics().heightPixels) {
                    toRemoveCircles.add(nextCircle);
                    player.subtractLife();
                }

                //if missile is in circle radius
                if(nextMissile.x >= nextCircle.cx - nextCircle.radius && nextMissile.x <= nextCircle.cx + nextCircle.radius
                        && nextMissile.y <= nextCircle.cy + nextCircle.radius) {
                    toRemoveMissiles.add(nextMissile);
                    v.vibrate(30);
                    if(soundPool != null) {
                        soundPool.play(sounds[0], .3f, .3f, 1, 0, 1);
                    }

                    points += nextCircle.health * 100;
                    toRemoveCircles.add(nextCircle);
                }
            }
        }

        if(powerUp != null) {
            if (powerUp.x >= player.cx - player.radius && powerUp.x <= player.cx + player.radius //set powerUp to 1 if touch
                    && powerUp.y >= player.cy) {
                player.setPowerUp(1);
                powerUp = null;
            }
        }

        //remove missiles and circles and stars
        missiles.removeAll(toRemoveMissiles);
        circles.removeAll(toRemoveCircles);
        stars.removeAll(toRemoveStars);
    }

    private void addCircle() {
        int cord = r.nextInt(getContext().getResources().getDisplayMetrics().widthPixels);
        if (cord + player.radius > getContext().getResources().getDisplayMetrics().widthPixels) {
            cord -= player.radius;
        }
        circles.add(new Circle(cord, 0f));
    }

    /**
     * Helper method for the "render loop"
     * @param canvas The canvas to draw on
     */
    public synchronized void render(Canvas canvas){

        if(canvas == null){return;}; //if we didn't get a valid canvas for whatever reason

        canvas.drawColor(Color.rgb(15, 15, 15)); //black out the background

        //draw stars
        for(Star star: stars) {
            canvas.drawCircle(star.x, star.y, star.radius, star.paint);
        }


        //draw missiles
        for(Missile missile: missiles) {
            canvas.drawRect(missile.x, missile.y, missile.x + missile.length, missile.y + missile.width, missile.paint);
        }

        //draw circle trail
        for(Circle circle: circles) {
            circle.paint.setAlpha(25);
            canvas.drawCircle(circle.cx, (int)(circle.cy - circle.radius * 1.5), (int) (circle.radius * .5), circle.paint);
        }

        for(Circle circle: circles) {
            circle.paint.setAlpha(40);
            canvas.drawCircle(circle.cx, (int)(circle.cy - circle.radius * 1.2), (int) (circle.radius * .6), circle.paint);
        }

        for(Circle circle: circles) {
            circle.paint.setAlpha(100);
            canvas.drawCircle(circle.cx, (int)(circle.cy - circle.radius * .6) , (int) (circle.radius * .9), circle.paint);
        }

        //draw circles
        for(Circle circle: circles) {
            circle.paint.setAlpha(255);
            canvas.drawCircle(circle.cx, circle.cy, circle.radius, circle.paint);
        }

        //draw player
        canvas.drawCircle(player.cx, player.cy, player.radius,player.paint);
        canvas.drawCircle(smallerPlayer.cx, smallerPlayer.cy, smallerPlayer.radius, smallerPlayer.paint);

        //draw powerUp
        if(powerUp != null) {
            canvas.drawText("C", powerUp.x, powerUp.y, powerUp.paint); //draw powerUp if not touched yet
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //create and start the background updating thread
        Log.d(TAG, "Creating new drawing thread");
        mThread = new Thread(mRunnable);
        mRunnable.setRunning(true); //turn on the runner
        mThread.start(); //start up the thread when surface is created

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        synchronized (mHolder) { //synchronized to keep this stuff atomic
            viewWidth = width;
            viewHeight = height;
            init();
            bmp = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888); //new buffer to draw on

        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
        mRunnable.setRunning(false); //turn off
        boolean retry = true;
        while(retry) {
            try {
                mThread.join();
                retry = false;
            } catch (InterruptedException e) {
                //will try again...
            }
        }
        Log.d(TAG, "Drawing thread shut down");
    }




    /**
     * An inner class representing a runnable that does the drawing. Animation timing could go in here.
     * http://obviam.net/index.php/the-android-game-loop/ has some nice details about using timers to specify animation
     */
    public class DrawingRunnable implements Runnable {

        private boolean isRunning; //whether we're running or not (so we can "stop" the thread)

        public void setRunning(boolean running){
            this.isRunning = running;
        }

        public void run() {
            Canvas canvas;
            while(isRunning)
            {
                canvas = null;
                try {
                    canvas = mHolder.lockCanvas(); //grab the current canvas
                    synchronized (mHolder) {
                        update(); //update the game
                        render(canvas); //redraw the screen
                    }
                }
                finally { //no matter what (even if something goes wrong), make sure to push the drawing so isn't inconsistent
                    if (canvas != null) {
                        mHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }
}