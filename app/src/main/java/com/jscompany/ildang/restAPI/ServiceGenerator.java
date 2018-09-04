package com.jscompany.ildang.restAPI;

import android.text.TextUtils;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

//    public static final String API_BASE_URL = "http://192.168.25.54:8090/restapi/";
//    public static final String API_BASE_URL = "http://192.168.0.6:8090/restapi/";
    public static final String API_BASE_URL = "https://ildangcall.com/restapi/";
    private static final String API_USER_NAME = "apiuser";
    private static final String API_PASSWORD = "Ildang2018#";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

//    public static <S> S createService(Class<S> serviceClass) {
//        return createService(serviceClass, null, null);
//    }

    public static <S> S createService( Class<S> serviceClass) {
        if (!TextUtils.isEmpty(API_USER_NAME)
                && !TextUtils.isEmpty(API_PASSWORD)) {
            String authToken = Credentials.basic(API_USER_NAME, API_PASSWORD);
            return createService(serviceClass, authToken);
        }
        return createService(serviceClass, null);
    }

    public static <S> S createService(
            Class<S> serviceClass, final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor =  new AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);

                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }

        return retrofit.create(serviceClass);
    }

}
