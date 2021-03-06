package com.octalsoftaware.archi.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by anandj on 6/6/2017.
 */

public class GestureScrollView extends ScrollView {
    GestureDetector myGesture;

    public GestureScrollView(Context context, GestureDetector gest) {
        super(context);
        myGesture = gest;
    }

    public GestureScrollView(Context context) {
        super(context);
    }

    public GestureScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (myGesture.onTouchEvent(ev))
            return true;
        else
            return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (myGesture.onTouchEvent(ev))
            return true;
        else
            return super.onInterceptTouchEvent(ev);
    }
}
