package com.example.becky.cs4295_pj_floatingboat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by Becky on 16/11/2015.
 */
public class SplashScreen extends Activity {

    /**
     * The thread to process splash screen events
     */
    private Thread mSplashThread;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Splash screen view
        setContentView(R.layout.splash_screen);

        final SplashScreen sPlashScreen = this;

        // The thread to wait for splash screen events
        mSplashThread =  new Thread(){
            @Override
            public void run(){
                try {

                    synchronized(this){
                        // Wait given period of time or exit on touch
                        if(MotionEvent.ACTION_DOWN==0) {
                            wait(4000);
                        }
                    }
                }
                catch(InterruptedException ex){
                }

                finish();

                // Run next activity
                Intent intent = new Intent();
                intent.setClass(sPlashScreen,StartUpMenu.class);
                startActivity(intent);
                //stop();
            }
        };

        mSplashThread.start();
    }

    /**
     * Processes splash screen touch events
     */

    @Override
      public boolean onTouchEvent(MotionEvent evt)
    {
        if(evt.getAction() == MotionEvent.ACTION_DOWN)
        {
            synchronized(mSplashThread){
                mSplashThread.notifyAll();
            }
        }
        return true;
    }


}