package com.octalsoftaware.archi.data.remote;

import android.support.annotation.NonNull;

import com.octalsoftaware.archi.MyApplication;
import com.octalsoftaware.archi.data.events.BaseEvent;
import com.octalsoftaware.archi.data.events.EmptyEvent;
import com.octalsoftaware.archi.data.events.ErrorEvent;
import com.octalsoftaware.archi.utils.constants.I;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Response;

/**
 * Created by bukhoriaqid on 11/12/16.
 */

abstract class BaseAPI {
    MyApplication app = MyApplication.getInstance();
    EventBus event = EventBus.getDefault();

    @NonNull
    EmptyEvent mEmptyEvent = new EmptyEvent();

    void handleResponse(@NonNull Response response, BaseEvent eventClass) {
        if (response.isSuccessful()) {
            if (response.code() == I.HTTP_NO_CONTENT) {
                event.post(mEmptyEvent);
            } else {
                event.post(eventClass);
            }
        } else {
            event.post(new ErrorEvent(response));
        }
    }
}
