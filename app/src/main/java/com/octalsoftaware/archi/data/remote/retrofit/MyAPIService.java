package com.octalsoftaware.archi.data.remote.retrofit;

import android.support.annotation.NonNull;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by bukhoriaqid on 11/12/16.
 */

public interface MyAPIService
{

    @NonNull
    @POST("login")
    Call<ResponseBody> login(@Body Map<String, Object> loginRequest);

    @NonNull
    @POST("home")
    Call<ResponseBody> homepage(@Body Map<String, Object> homepage);

    @NonNull
    @POST("cancel_record")
    Call<ResponseBody> cancel_record(@Body Map<String, Object> cancel_record);

    @NonNull
    @POST("reopen_record")
    Call<ResponseBody> reopen_record(@Body Map<String, Object> reopen_record);

    @NonNull
    @POST("get_notes")
    Call<ResponseBody> getNote(@Body Map<String, Object> get_notes);

    @NonNull
    @POST("add_notes")
    Call<ResponseBody> addNote(@Body Map<String, Object> get_notes);

    @NonNull
    @POST("locations")
    Call<ResponseBody> getHospitalLocation();

    @NonNull
    @POST("images_list")
    Call<ResponseBody> getImagesList(@Body Map<String,Object> images_list );

    @NonNull
    @POST("images_delete")
    Call<ResponseBody> deletePatientImage(@Body Map<String,Object> images_list );

    @NonNull
    @Multipart
    @POST("image_add")
    Call<ResponseBody> imageAdd(@PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part file);

    @NonNull
    @POST("all_list")
    Call<ResponseBody> allList(@Body Map<String,Object> stringObjectMap);

    @NonNull
    @POST("mode_anesthesia")
    Call<ResponseBody> modeOfAnesthesia(@Body Map<String,Object> stringObjectMap);

    @NonNull
    @POST("save_charge")
    Call<ResponseBody> saveChargeInformation(@Body Map<String,Object> stringObjectMap);

    @NonNull
    @POST("save_qi")
    Call<ResponseBody> saveQualityInformation(@Body Map<String,Object> stringObjectMap);

    @NonNull
    @POST("add_patient")
    Call<ResponseBody> addPatient(@Body Map<String,Object> stringObjectMap);

    @NonNull
    @POST("procedures_list")
    Call<ResponseBody> proceduresList(@Body Map<String,Object> stringObjectMap);

    @NonNull
    @POST("procedures_list")
    Call<ResponseBody> proceduresListAdapter(@Body Map<String,Object> stringObjectMap);

    @NonNull
    @POST("save_invasive_charge")
    Call<ResponseBody> saveInvasiveCharge(@Body Map<String,Object> stringObjectMap);

    @NonNull
    @POST("qi_details")
    Call<ResponseBody> getSaveQi(@Body Map<String,Object> stringObjectMap);

    @NonNull
    @POST("get_saved_charge")
    Call<ResponseBody> getSaveCharge(@Body Map<String,Object> stringObjectMap);

    @NonNull
    @POST("charge_details")
    Call<ResponseBody> getInvasiveLine(@Body Map<String,Object> stringObjectMap);


   /* @GET ("users/{id}")
    public Call<JsonObject> forgotPassword(@Path("id") String id);*/
}
