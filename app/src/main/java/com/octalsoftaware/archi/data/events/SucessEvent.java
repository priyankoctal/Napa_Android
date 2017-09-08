package com.octalsoftaware.archi.data.events;

import android.support.annotation.Nullable;

import com.octalsoftaware.archi.utils.Util;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by anandj on 4/26/2017.
 */

public class SucessEvent extends BaseEvent {

    Response<ResponseBody> responce;

    public SucessEvent() {
    }

    public SucessEvent(Response<ResponseBody> responce) {
        this.responce = responce;
    }

    @Nullable
    public String getResponce() {
        return Util.convertRetrofitResponce(responce);
    }
}
