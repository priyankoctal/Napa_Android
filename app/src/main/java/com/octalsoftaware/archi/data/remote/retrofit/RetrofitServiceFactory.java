package com.octalsoftaware.archi.data.remote.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Anand Jain on 11/12/16.
 */

public class RetrofitServiceFactory {
    private static final String BASE_URL = "http://192.168.1.83/projects/coding_system/Api/";
    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(new DataTypeAdapterFactory()).create();
    private static final Retrofit.Builder sBuilder = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(
            GsonConverterFactory.create(gson));

    public static Retrofit sRetrofit;

   /* public static <S> S createService(Class<S> serviceClass) {
        final String authHeader = "";
       *//* if (DataManager.getUserToken() != null) {
            authHeader = "Bearer " + DataManager.getUserToken();
        } else {
            authHeader = "";
        }*//*

        httpClient.addInterceptor(new ResponseInterceptor());
       *//* //add authorization header
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request lOriginalRequest = chain.request();
                Request lRequest = lOriginalRequest.newBuilder().header("Authorization", authHeader)
                        .method(lOriginalRequest.method(), lOriginalRequest.body()).build();

                return chain.proceed(lRequest);
            }
        });*//*

        OkHttpClient lClient = httpClient.build();
        sRetrofit = sBuilder.client(lClient).build();
        return sRetrofit.create(serviceClass);
    }*/
    public static Retrofit getRequestQueue() {
        // initialize the request queue, the queue instance will be created when it is accessed for the first time
        if (sRetrofit == null) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(60, TimeUnit.SECONDS);
            builder.readTimeout(60, TimeUnit.SECONDS);
            builder.writeTimeout(60, TimeUnit.SECONDS);


            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

            // Can be Level.BASIC, Level.HEADERS, or Level.BODY
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            // add logging as last interceptor
            builder.addInterceptor(httpLoggingInterceptor); // <-- this is the important line!
            //builder.networkInterceptors().add(httpLoggingInterceptor);
            OkHttpClient okHttpClient = builder.build();

            sRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return sRetrofit;
    }

}
