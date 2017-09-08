package com.octalsoftaware.archi.utils;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by anandj on 6/6/2017.
 */

public class MyOnTouch implements View.OnTouchListener {
    Context mContext;
   public MyOnTouch(Context context){
        this.mContext = context;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return pageFlip(v, event);
    }

    public boolean pageFlip(View v, MotionEvent event) {
        int startX = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float currentX = event.getX();
                if (startX > currentX + 150 ) {
                    // nextText(v);
                }
                if (startX  < currentX - 150) {
                    ((Activity) mContext).finish();
                    //previousText(v);
                }
            default:
                break;
        }
        return false;
    }
}
