package com.example.becky.cs4295_pj_floatingboat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Becky on 16/11/2015.
 */
public class StartUpMenu extends Activity implements View.OnClickListener {
    ImageButton Button;
    final StartUpMenu sum = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        ImageButton but_toGame = (ImageButton)findViewById(R.id.imgbut_game);
        but_toGame.setOnClickListener(this);
    }

    public void htp_onClick(View v){
        Intent myTriggerActivityIntent=new Intent(sum,HowToPlay.class);
        startActivity(myTriggerActivityIntent);
        finish();
    }

    @Override
    public void onClick(View v) {

        new AlertDialog.Builder(this)
                .setTitle("Are you ready?!")
                //.setMessage("Are you sure you want to exit?")
                //.setNegativeButton(android.R.string.no, null)
                .setPositiveButton("Yeah!!!", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent myTriggerActivityIntent = new Intent(sum, AccSensor.class);
                        startActivity(myTriggerActivityIntent);
                    }
                }).create().show();
        // TODO Auto-generated method stub
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                       StartUpMenu.super.onBackPressed();
                        System.exit(0);
                        finish();
                    }
                }).create().show();
    }


    public void onExit(View v) {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        //StartUpMenu.super.onBackPressed();
                        System.exit(0);
                    }
                }).create().show();
    }

    public void pro_onClick(View view) {
        Intent myTriggerActivityIntent=new Intent(sum,Profile.class);
        startActivity(myTriggerActivityIntent);
        finish();
    }

    public void s_onClick(View view) {
        Intent myTriggerActivityIntent=new Intent(sum,Score.class);
        startActivity(myTriggerActivityIntent);
        finish();
    }

    /**
     * Called when you are no longer visible to the user.  You will next
     * receive either {@link #onRestart}, {@link #onDestroy}, or nothing,
     * depending on later user activity.
     * <p/>
     * <p>Note that this method may never be called, in low memory situations
     * where the system does not have enough memory to keep your activity's
     * process running after its {@link #onPause} method is called.
     * <p/>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onRestart
     * @see #onResume
     * @see #onSaveInstanceState
     * @see #onDestroy
     */
    @Override
    protected void onStop() {
        super.onStop();
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
    }
}
