package com.octalsoftaware.archi.views.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import com.octalsoftaware.archi.utils.MySharedPreferences;
import com.octalsoftaware.archi.utils.constants.I;
import com.octalsoftaware.archi.utils.constants.S;

import io.fabric.sdk.android.Fabric;

public class LaunchActivity extends AppCompatActivity {
    @Nullable
    private Context context = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // fabric initialization
        Fabric.with(this, new Crashlytics());

        // full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // initialization context
        context = this;

        final Handler handler = new Handler();
        // hold splash screen
        handler.postDelayed(new Runnable() {
           @Override
           public void run() {
             // check user is login or not
               Intent intent;
               if(MySharedPreferences.getPreferences(context, S.user_id).contentEquals("")){
                   intent = new Intent(LaunchActivity.this,LoginActivity.class);
               }
               else
                   intent = new Intent(LaunchActivity.this,HomeActivity.class);
               // start next activity
               startActivity(intent);
               finish();
               handler.removeCallbacks(this);
           }
       }, I.SPLASH_DISPLAY_LENGTH);
    }
}
