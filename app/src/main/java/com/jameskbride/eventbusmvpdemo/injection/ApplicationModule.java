package com.jameskbride.eventbusmvpdemo.injection;

import com.jameskbride.eventbusmvpdemo.BuildConfig;
import com.jameskbride.eventbusmvpdemo.main.MainActivityImpl;
import com.jameskbride.eventbusmvpdemo.network.BurritosToGoApi;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import javax.inject.Singleton;

@Module
class ApplicationModule {

    @Provides
    @Singleton
    public OkHttpClient makeOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    @Provides
    @Singleton
    public Retrofit makeRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .baseUrl(BuildConfig.BASE_URL)
                .build();
    }

    @Provides
    @Singleton
    public BurritosToGoApi makeBurritosToGoApi(Retrofit retrofit) {
        return retrofit.create(BurritosToGoApi.class);
    }

    @Provides
    public MainActivityImpl makeMainActivityImpl(BurritosToGoApi burritosToGoApi) {
        return new MainActivityImpl(burritosToGoApi);
    }
}