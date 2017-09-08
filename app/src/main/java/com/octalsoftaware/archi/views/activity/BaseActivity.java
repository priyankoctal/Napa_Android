package com.octalsoftaware.archi.views.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by anandj on 4/14/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public EventBus event;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        event = EventBus.getDefault();
    }

    /**
     * method used for first event initialization & manipulation
     */
    public abstract void initEvent();

    protected void hideKeyboard ()   {
        View view = getCurrentFocus();
        if (view != null)
        {
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    public void jumpToHome(View v){
        Intent intent = new Intent(this,HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
