package com.example.becky.cs4295_pj_floatingboat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Becky on 16/11/2015.
 */
public class AccSensor extends Activity {

    boolean APP_EXIT_FLAG;
    static boolean APP_RUN_FLAG;
    public GameView gameView;
    public SensorManager sensorManager;
    public Sensor accSensor;
    int screenWidth;
    int screenHeight;
    public static Timer timer;
    public static TimerTask timerTask = null;
    public static Handler handler = new Handler();
    public static Runnable abletorun;
    android.graphics.PointF objectPosition;
    android.graphics.PointF objectSpeed;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        APP_RUN_FLAG=true;
        getWindow().setFlags(0xFFFFFFFF, WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i=getIntent();
        gameView = (GameView) findViewById(R.id.gameView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        objectPosition = new android.graphics.PointF();
        objectSpeed = new android.graphics.PointF();

        objectPosition.x = screenWidth/2;
        objectPosition.y = screenHeight/2;
        objectSpeed.x = 0;
        objectSpeed.y = 0;

        ((SensorManager)getSystemService(Context.SENSOR_SERVICE)).registerListener(
                new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent event) {
                        objectSpeed.x = -event.values[0];
                        objectSpeed.y = event.values[1];
                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
                },
                ((SensorManager)getSystemService(Context.SENSOR_SERVICE))
                        .getSensorList(Sensor.TYPE_ACCELEROMETER).get(0), SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Exit");
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Called as part of the activity lifecycle when an activity is going into
     * the background, but has not (yet) been killed.  The counterpart to
     * {@link #onResume}.
     * <p/>
     * <p>When activity B is launched in front of activity A, this callback will
     * be invoked on A.  B will not be created until A's {@link #onPause} returns,
     * so be sure to not do anything lengthy here.
     * <p/>
     * <p>This callback is mostly used for saving any persistent state the
     * activity is editing, to present a "edit in place" model to the user and
     * making sure nothing is lost if there are not enough resources to start
     * the new activity without first killing this one.  This is also a good
     * place to do things like stop animations and other things that consume a
     * noticeable amount of CPU in order to make the switch to the next activity
     * as fast as possible, or to close resources that are exclusive access
     * such as the camera.
     * <p/>
     * <p>In situations where the system needs more memory it may kill paused
     * processes to reclaim resources.  Because of this, you should be sure
     * that all of your state is saved by the time you return from
     * this function.  In general {@link #onSaveInstanceState} is used to save
     * per-instance state in the activity and this method is used to store
     * global persistent data (in content providers, files, etc.)
     * <p/>
     * <p>After receiving this call you will usually receive a following call
     * to {@link #onStop} (after the next activity has been resumed and
     * displayed), however in some cases there will be a direct call back to
     * {@link #onResume} without going through the stopped state.
     * <p/>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onResume
     * @see #onSaveInstanceState
     * @see #onStop
     */
    @Override
    protected void onPause() {
        super.onPause();
        APP_RUN_FLAG=false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle() == "Exit"){
            //super.onPause();
            APP_RUN_FLAG=false;
            new AlertDialog.Builder(this)
                    .setTitle("Return?")
                    .setMessage("Return to main menu?")
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {APP_RUN_FLAG=true;}})
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            //AccSensor.super.onBackPressed();
                            APP_RUN_FLAG = false;
                            System.exit(0);

                        }
                    }).create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        APP_RUN_FLAG=false;
                    //
                    //        abletorun = new Runnable() {
                    //            @Override
                    //            public void run() {
                    //                handler.post(new Runnable() {
                    //                    public void run() {
                    //                        objectPosition.x += 0;
                    //                        objectPosition.y += 0;
                    //
                    //                        GameView.boat.x += 0;
                    //                        GameView.boat.y += 0;
                    //                        gameView.invalidate();
                    //                        //GameView.stopUpdatePositions();
                    //                    }});
                    //                //GameView.stopUpdatePositions();
                    //                handler.removeCallbacks(this, 1000);
                    //            }
                    //        };
                           // mHandler.removeCallbacksAndMessages(runnable);
        new AlertDialog.Builder(this)
                .setTitle("Return?")
                .setMessage("Return to main menu?")
                .setNegativeButton(android.R.string.no,new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        APP_RUN_FLAG=true;
                    }
                    })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        //AccSensor.super.onBackPressed();
                        System.exit(0);
                    }
                }).create().show();
    }
    public static void exit()
    {
        System.exit(0);
    }
    @Override
    public void onResume()
    {
        if(AccSensor.getRunFlag()) {
            abletorun = new Runnable() {
                @Override
                public void run() {
                        objectPosition.x += objectSpeed.x;
                        objectPosition.y += objectSpeed.y;

                        if (objectPosition.x > screenWidth) {
                            objectPosition.x = 0;
                        }
                        if (objectPosition.y > screenHeight) {
                            objectPosition.y = 0;
                        }
                        if (objectPosition.x < 0) {
                            objectPosition.x = screenWidth;
                        }
                        if (objectPosition.y < 0) {
                            objectPosition.y = screenHeight;
                        }

                    if(AccSensor.getRunFlag()) {
                        GameView.boat.x = objectPosition.x;
                        GameView.boat.y = objectPosition.y;
                    GameView.updatePositions();
                    }
                    handler.post(new Runnable() {
                        public void run() {
                            gameView.invalidate();
                        }
                    });
                    handler.postDelayed(this, 10);
                }
            };
        }
        super.onResume();
    }

    @Override
    protected void onDestroy()
    {

        APP_RUN_FLAG=false;
        super.onDestroy();
        gameView.releaseResources();
        System.runFinalizersOnExit(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    static boolean getRunFlag() {
        return APP_RUN_FLAG;
    }
}
