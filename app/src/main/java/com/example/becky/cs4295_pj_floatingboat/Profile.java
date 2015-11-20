package com.example.becky.cs4295_pj_floatingboat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Becky on 19/11/2015.
 */
public class Profile extends Activity {

    ArrayList<String> username;
    ArrayList<Date> createDate;
    ArrayList<profile_detail> profile_d;
    ArrayAdapter<String> adapter;
    int time_count=1;
    String time_count_second;
    Timer T;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        username = new ArrayList<>();

        final TextView tv_time = (TextView) findViewById(R.id.time);

        T =new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (time_count%60<10) { time_count_second="0"+time_count%60;}
                            else{ time_count_second = String.valueOf(time_count%60);}
                        if(time_count>59&time_count<(60*60))
                        {
                            tv_time.setText("Time Used: "+time_count/60+":"+time_count_second);
                        }
                        else {
                            tv_time.setText("Time Used: 00:" + time_count_second);
                        }
                        time_count++;
                    }
                });
            }
        }, 1000, 1000);
    }
    public void t_onClick(View v)
    {
        //this is 'Pause' button click listener
        T.cancel();
    }

//
//        long tStart = System.nanoTime();
//
//        long tEnd = System.nanoTime();
//        long tDelta = tEnd - tStart;
//        double elapsedSeconds = tDelta / 1000.0;
//
//        tv_time.setText("Time"+elapsedSeconds);

//        new CountDownTimer(30000, 1000) {
//            public void onTick(long millisUntilFinished) {
//                tv_time.setText("seconds remaining: " + millisUntilFinished / 1000);
//            }
//            public void onFinish() {
//                tv_time.setText("done!");
//            }
//        }.start();
    /**
     * Called after {@link #onRestoreInstanceState}, {@link #onRestart}, or
     * {@link #onPause}, for your activity to start interacting with the user.
     * This is a good place to begin animations, open exclusive-access devices
     * (such as the camera), etc.
     * <p/>
     * <p>Keep in mind that onResume is not the best indicator that your activity
     * is visible to the user; a system window such as the keyguard may be in
     * front.  Use {@link #onWindowFocusChanged} to know for certain that your
     * activity is visible to the user (for example, to resume a game).
     * <p/>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onRestoreInstanceState
     * @see #onRestart
     * @see #onPostResume
     * @see #onPause
     */

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    final Profile pfile = this;

    public void return_onClick(View v) {
        Intent myTriggerActivityIntent = new Intent(pfile, StartUpMenu.class);
        startActivity(myTriggerActivityIntent);
        finish();
    }


    @Override
    public void onBackPressed() {
        Intent myTriggerActivityIntent = new Intent(pfile, StartUpMenu.class);
        startActivity(myTriggerActivityIntent);
        Profile.super.onBackPressed();
        finish();
    }


}

