package com.octalsoftaware.archi;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.octalsoftaware.archi.data.remote.retrofit.MyAPIService;
import com.octalsoftaware.archi.data.remote.retrofit.RetrofitServiceFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anandj on 4/12/2017.
 */

public class MyApplication extends Application {
    private static MyApplication   sApp;
    public MyAPIService mAPIService;
    private Retrofit retrofit;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public static MyApplication getInstance(){
        if(sApp==null)
            sApp = new MyApplication();

        return sApp;

        //epschencman@yahoo.com
        //qwerty
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAPIService = RetrofitServiceFactory.getRequestQueue().create(MyAPIService.class);
        initImageLoader(this);
    //    iniRealmDatabase(this);

    }
    public Retrofit getRequestQueue() {
        final String BASE_URL = "https://aris-stage.napacloud.net/Api/";//"http://192.168.1.83/projects/coding_system/Api/";
        // initialize the request queue, the queue instance will be created when it is accessed for the first time
        if (retrofit == null) {
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

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
    public static void initImageLoader(@NonNull Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
}
