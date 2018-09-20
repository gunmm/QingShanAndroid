package com.qsjt.qingshan.http;

import android.util.Log;

import com.qsjt.qingshan.application.MyApplication;
import com.qsjt.qingshan.config.Config;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @author LiYouGui
 */

public class RxRetrofit {

    private static RxRetrofit instance;

    private ApiService service;

    private RxRetrofit() {
        //日志拦截器
        HttpLoggingInterceptor loggingInterceptor
                = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("NetTask", message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(ApiService.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(ApiService.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(loggingInterceptor)
                .build();

        //Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(ResponseConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Config.getInstance().getApiServer())
                .build();

        service = retrofit.create(ApiService.class);
    }

    //单利模式
    public static ApiService getApiService() {
        if (instance == null) {
            synchronized (RxRetrofit.class) {
                if (instance == null) {
                    instance = new RxRetrofit();
                }
            }
        }
        return instance.service;
    }

    public static void clean() {
        instance = null;
    }
}
