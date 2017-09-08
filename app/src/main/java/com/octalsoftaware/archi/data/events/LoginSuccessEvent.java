package com.octalsoftaware.archi.data.events;


import android.support.annotation.Nullable;

import com.octalsoftaware.archi.utils.Util;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by bukhoriaqid on 11/11/16.
 */

public class LoginSuccessEvent extends BaseEvent {
    Response<ResponseBody> responce;

    public LoginSuccessEvent(){
    }
    public LoginSuccessEvent(Response<ResponseBody> responce){
        this.responce = responce;
    }

    @Nullable
    public String getResponce(){
      return Util.convertRetrofitResponce(responce);
    }
}

