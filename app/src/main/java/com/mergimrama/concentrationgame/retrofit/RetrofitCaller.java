package com.mergimrama.concentrationgame.retrofit;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCaller {
    private static final String TAG = RetrofitCaller.class.getSimpleName();
    private static String apiKey = "e15d66bc997eb1b534e9e18e54181e15";
    private static String BASE_URL = "https://api.flickr.com/services/rest/";

    public static <S> S call(Class<S> serviceClass) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(1, TimeUnit.MINUTES);
        httpClientBuilder.writeTimeout(15, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(30, TimeUnit.SECONDS);

        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl httpUrl = request.url().newBuilder()
                        .addQueryParameter("api_key", apiKey)
                        .addQueryParameter("format", "json")
                        .addQueryParameter("nojsoncallback", "1")
                        .addQueryParameter("extras", "url_s")
                        .build();
                request = request.newBuilder().url(httpUrl).build();
                return chain.proceed(request);
            }
        });

        httpClientBuilder.addInterceptor(loggingInterceptor);

        OkHttpClient client = httpClientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).client(client).build();
        return retrofit.create(serviceClass);
    }
}
