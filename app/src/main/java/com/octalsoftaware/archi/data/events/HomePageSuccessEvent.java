package com.octalsoftaware.archi.data.events;

import android.support.annotation.Nullable;

import com.octalsoftaware.archi.utils.Util;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by anandj on 4/24/2017.
 */

public class HomePageSuccessEvent extends BaseEvent {

    Response<ResponseBody> responce;

    public HomePageSuccessEvent() {
    }

    public HomePageSuccessEvent(Response<ResponseBody> responce) {
        this.responce = responce;
    }

    @Nullable
    public String getResponce() {
        return Util.convertRetrofitResponce(responce);
    }
}
