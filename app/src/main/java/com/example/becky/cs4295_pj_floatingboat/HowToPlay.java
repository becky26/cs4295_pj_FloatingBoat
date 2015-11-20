package com.example.becky.cs4295_pj_floatingboat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Becky on 18/11/2015.
 */
// implements View.OnClickListener
public class HowToPlay extends Activity{

    final HowToPlay htp = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.how_to_play);
    //Button but_return = (Button)findViewById(R.id.but_return);
    //but_return.setOnClickListener(this);
    }

    public void return_onClick(View v)
    {
        Intent myTriggerActivityIntent=new Intent(htp, StartUpMenu.class);
        startActivity(myTriggerActivityIntent);
        finish();
    }


    @Override
    public void onBackPressed() {
        Intent myTriggerActivityIntent=new Intent(htp, StartUpMenu.class);
        startActivity(myTriggerActivityIntent);
        HowToPlay.super.onBackPressed();
        finish();
    }


}
