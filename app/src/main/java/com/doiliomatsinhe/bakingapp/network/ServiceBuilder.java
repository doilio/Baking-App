package com.doiliomatsinhe.bakingapp.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceBuilder {

    // Create a Logger
    private static HttpLoggingInterceptor logger = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    // Create Http Client
    private static OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder().addInterceptor(logger);

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient.build());

    private static Retrofit retrofit = builder.build();

    public static <Doilio> Doilio BuildService(Class<Doilio> serviceType) {
        return retrofit.create(serviceType);
    }
}