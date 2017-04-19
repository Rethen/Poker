package com.poseidon.pokers.data.net;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.poseidon.pokers.BuildConfig;
import com.poseidon.pokers.domain.log.HttpLoggingInterceptor;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 42524 on 2017/4/12.
 */
@Singleton
public class HttpApiManager {

    private final static String HOST = "hkg02-sys-web199.hkg02.hipokers.com";

    private final static int PORT = 8080;

    private final static String SCHEME = "http";

    private Gson gson;

    private OkHttpClient httpClient;

    private HttpUrl httpUrl;

    private Retrofit retrofit;


    @Inject
    HttpApiManager(Gson gson) {
        this.gson = gson;
        genericHttpUrl();
        genericClient();
        genericRetrofit();
    }

    private void genericHttpUrl() {
        httpUrl = new HttpUrl.Builder().scheme(SCHEME).host(HOST).port(PORT).build();
    }


    private OkHttpClient genericClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Content-Type", "application/json; charset=UTF-8")
                                .addHeader("Accept", "*/*")
                                .build();
                        return chain.proceed(request);
                    }
                })
                .build();
        return httpClient;
    }


    private void genericRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(httpUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public <T> T getRemoteRespositroy(Class<T> clazz) {
        return retrofit.create(clazz);
    }


}
