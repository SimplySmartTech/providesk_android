package com.simplysmart.lib.config;

import com.simplysmart.lib.global.AppSessionData;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(LibraryConstant.SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit().create(serviceClass);
    }

    public static Retrofit retrofit() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .readTimeout(LibraryConstant.READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(LibraryConstant.CONNECTION_TIMEOUT, TimeUnit.SECONDS);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Content-Type", LibraryConstant.CONTENT_TYPE)
                        .header("Accept", LibraryConstant.ACCEPT_TYPE)
                        .header("X-Api-Key", AppSessionData.getInstance().getApiKey() != null ? AppSessionData.getInstance().getApiKey() : "")
                        .header("Authorization", AppSessionData.getInstance().getAuthToken() != null ? AppSessionData.getInstance().getAuthToken() : "")
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        });

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(LibraryConstant.isDebuggable ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        httpClient.addInterceptor(interceptor);

        OkHttpClient client = httpClient.build();
        return builder.client(client).build();
    }

}