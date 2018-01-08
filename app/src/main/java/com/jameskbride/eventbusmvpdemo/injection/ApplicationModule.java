package com.jameskbride.eventbusmvpdemo.injection;

import com.jameskbride.eventbusmvpdemo.BuildConfig;
import com.jameskbride.eventbusmvpdemo.main.MainActivityImpl;
import com.jameskbride.eventbusmvpdemo.main.MainActivityPresenter;
import com.jameskbride.eventbusmvpdemo.network.BurritosToGoApi;
import com.jameskbride.eventbusmvpdemo.network.service.BurritosToGoService;

import org.greenrobot.eventbus.EventBus;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Named;
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
    @Named("process")
    @Singleton
    public Scheduler makeProcessScheduler() {
        return Schedulers.io();
    }

    @Provides
    @Named("main")
    @Singleton
    public Scheduler makeAndroidScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Singleton
    public BurritosToGoApi makeBurritosToGoApi(Retrofit retrofit) {
        return retrofit.create(BurritosToGoApi.class);
    }

    @Provides
    @Singleton
    public BurritosToGoService makeBurritosToGoService(
            EventBus eventBus,
            BurritosToGoApi burritosToGoApi,
            @Named("process") Scheduler processScheduler,
            @Named("main") Scheduler androidScheduler) {
        return new BurritosToGoService(eventBus, burritosToGoApi, processScheduler, androidScheduler);
    }

    @Provides
    @Singleton
    public EventBus makeEventBus() {
        return EventBus.getDefault();
    }

    @Provides
    public MainActivityPresenter makeMainActivityPresenter(EventBus eventBus) {
        return new MainActivityPresenter(eventBus);
    }

    @Provides
    public MainActivityImpl makeMainActivityImpl(MainActivityPresenter presenter) {
        return new MainActivityImpl(presenter);
    }
}