package com.octalsoftaware.archi.data.events;

import android.support.annotation.Nullable;

import com.octalsoftaware.archi.utils.Util;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by anandj on 4/28/2017.
 */

public class PatientDetailsSucessEvent  extends BaseEvent {

    Response<ResponseBody> responce;

    public PatientDetailsSucessEvent() {
    }

    public PatientDetailsSucessEvent(Response<ResponseBody> responce) {
        this.responce = responce;
    }

    @Nullable
    public String getResponce() {
        return Util.convertRetrofitResponce(responce);
    }
}
