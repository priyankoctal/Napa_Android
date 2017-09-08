package com.octalsoftaware.archi.data.remote;

import android.support.annotation.NonNull;

import com.octalsoftaware.archi.MyApplication;
import com.octalsoftaware.archi.data.events.ErrorEvent;
import com.octalsoftaware.archi.data.events.HomePageSuccessEvent;
import com.octalsoftaware.archi.data.events.LoginSuccessEvent;
import com.octalsoftaware.archi.data.events.SucessEvent;
import com.octalsoftaware.archi.data.remote.retrofit.MyAPIService;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Anand Jain on 11/11/16.
 */

public class AuthAPI extends BaseAPI {

    public void login(Map<String, Object> stringObjectMap) {
        MyApplication.getInstance().getRequestQueue().create(MyAPIService.class).login(stringObjectMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleResponse(response, new LoginSuccessEvent(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, @NonNull Throwable t) {
                event.post(new ErrorEvent(t));
            }
        });
    }

    public void homepage(Map<String, Object> stringObjectMap) {
        MyApplication.getInstance().getRequestQueue().create(MyAPIService.class).homepage(stringObjectMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleResponse(response, new HomePageSuccessEvent(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, @NonNull Throwable t) {
                event.post(new ErrorEvent(t));
            }
        });
    }

    public void cancelcase(Map<String, Object> stringObjectMap) {
        MyApplication.getInstance().getRequestQueue().create(MyAPIService.class).cancel_record(stringObjectMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleResponse(response, new SucessEvent(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, @NonNull Throwable t) {
                event.post(new ErrorEvent(t));
            }
        });
    }

    public void reopenCase(Map<String, Object> stringObjectMap, final int status) {
        MyApplication.getInstance().getRequestQueue().create(MyAPIService.class).reopen_record(stringObjectMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (status == 1)
                    handleResponse(response, new HomePageSuccessEvent(response));
                else
                    handleResponse(response, new SucessEvent(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, @NonNull Throwable t) {
                event.post(new ErrorEvent(t));
            }
        });
    }

    public void getNote(Map<String, Object> stringObjectMap) {
        MyApplication.getInstance().getRequestQueue().create(MyAPIService.class).getNote(stringObjectMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleResponse(response, new SucessEvent(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, @NonNull Throwable t) {
                event.post(new ErrorEvent(t));
            }
        });
    }

    public void addNote(Map<String, Object> stringObjectMap) {
        MyApplication.getInstance().getRequestQueue().create(MyAPIService.class).addNote(stringObjectMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleResponse(response, new SucessEvent(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, @NonNull Throwable t) {
                event.post(new ErrorEvent(t));
            }
        });
    }

    public void getHospitalLocation() {
        MyApplication.getInstance().getRequestQueue().create(MyAPIService.class).getHospitalLocation().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleResponse(response, new SucessEvent(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, @NonNull Throwable t) {
                event.post(new ErrorEvent(t));
            }
        });
    }

    public void getImagesList(Map<String, Object> stringObjectMap) {
        MyApplication.getInstance().getRequestQueue().create(MyAPIService.class).getImagesList(stringObjectMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleResponse(response, new SucessEvent(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, @NonNull Throwable t) {
                event.post(new ErrorEvent(t));
            }
        });
    }

    public void deletePatientImage(Map<String, Object> stringObjectMap) {
        MyApplication.getInstance().getRequestQueue().create(MyAPIService.class).deletePatientImage(stringObjectMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleResponse(response, new SucessEvent(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, @NonNull Throwable t) {
                event.post(new ErrorEvent(t));
            }
        });
    }

    public void addImage(Map<String, RequestBody> partMap, MultipartBody.Part file) {
        MyApplication.getInstance().getRequestQueue().create(MyAPIService.class).imageAdd(partMap, file).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleResponse(response, new SucessEvent(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, @NonNull Throwable t) {
                event.post(new ErrorEvent(t));
            }
        });
    }

    public void allList(Map<String, Object> stringObjectMap) {
        MyApplication.getInstance().getRequestQueue().create(MyAPIService.class).allList(stringObjectMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleResponse(response, new SucessEvent(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, @NonNull Throwable t) {
                event.post(new ErrorEvent(t));
            }
        });
    }

    public void modeOfAnesthesia(Map<String, Object> stringObjectMap) {
        MyApplication.getInstance().getRequestQueue().create(MyAPIService.class).modeOfAnesthesia(stringObjectMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleResponse(response, new SucessEvent(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, @NonNull Throwable t) {
                event.post(new ErrorEvent(t));
            }
        });
    }

    public void saveChargeInformation(Map<String, Object> stringObjectMap) {
        MyApplication.getInstance().getRequestQueue().create(MyAPIService.class).saveChargeInformation(stringObjectMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleResponse(response, new SucessEvent(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, @NonNull Throwable t) {
                event.post(new ErrorEvent(t));
            }
        });
    }

    public void saveQualityInformation(Map<String, Object> stringObjectMap) {
        MyApplication.getInstance().getRequestQueue().create(MyAPIService.class).saveQualityInformation(stringObjectMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleResponse(response, new SucessEvent(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, @NonNull Throwable t) {
                event.post(new ErrorEvent(t));
            }
        });
    }

    public void addPatient(Map<String, Object> stringObjectMap) {
        MyApplication.getInstance().getRequestQueue().create(MyAPIService.class).addPatient(stringObjectMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleResponse(response, new SucessEvent(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, @NonNull Throwable t) {
                event.post(new ErrorEvent(t));
            }
        });
    }

    public void proceduresList(Map<String, Object> stringObjectMap) {
        MyApplication.getInstance().getRequestQueue().create(MyAPIService.class).proceduresList(stringObjectMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleResponse(response, new SucessEvent(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, @NonNull Throwable t) {
                event.post(new ErrorEvent(t));
            }
        });
    }

    public void saveInvasiveCharge(Map<String, Object> stringObjectMap) {
        MyApplication.getInstance().getRequestQueue().create(MyAPIService.class).saveInvasiveCharge(stringObjectMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleResponse(response, new SucessEvent(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, @NonNull Throwable t) {
                event.post(new ErrorEvent(t));
            }
        });
    }

    public void getSaveQi(Map<String, Object> stringObjectMap) {
        MyApplication.getInstance().getRequestQueue().create(MyAPIService.class).getSaveQi(stringObjectMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleResponse(response, new SucessEvent(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, @NonNull Throwable t) {
                event.post(new ErrorEvent(t));
            }
        });
    }

    public void getSaveCharge(Map<String, Object> stringObjectMap) {
        MyApplication.getInstance().getRequestQueue().create(MyAPIService.class).getSaveCharge(stringObjectMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleResponse(response, new SucessEvent(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, @NonNull Throwable t) {
                event.post(new ErrorEvent(t));
            }
        });
    }

    public void getInvasiveLine(Map<String, Object> stringObjectMap) {
        MyApplication.getInstance().getRequestQueue().create(MyAPIService.class).getInvasiveLine(stringObjectMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleResponse(response, new SucessEvent(response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, @NonNull Throwable t) {
                event.post(new ErrorEvent(t));
            }
        });
    }

}
