package com.jameskbride.eventbusmvpdemo.injection

import com.jameskbride.eventbusmvpdemo.BuildConfig
import com.jameskbride.eventbusmvpdemo.main.MainActivityImpl
import com.jameskbride.eventbusmvpdemo.main.MainActivityPresenter
import com.jameskbride.eventbusmvpdemo.network.BurritosToGoApi
import com.jameskbride.eventbusmvpdemo.network.service.BurritosToGoService
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @Singleton
    fun makeOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
    }

    @Provides
    @Singleton
    fun makeRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .baseUrl(BuildConfig.BASE_URL)
                .build()
    }

    @Provides
    @Singleton
    fun makeBurritosToGoApi(retrofit: Retrofit): BurritosToGoApi {
        return retrofit.create(BurritosToGoApi::class.java)
    }

    @Provides
    @Named("process")
    @Singleton
    fun makeProcessScheduler(): Scheduler {
        return Schedulers.io()
    }

    @Provides
    @Named("main")
    @Singleton
    fun makeAndroidScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    @Provides
    @Singleton
    fun makeEventBus():EventBus {
        return EventBus.getDefault()
    }

    @Provides
    @Singleton
    fun makeBurritosToGoService(
            eventBus: EventBus,
            burritosToGoApi: BurritosToGoApi,
            @Named("process") processScheduler: Scheduler,
            @Named("main") androidScheduler: Scheduler): BurritosToGoService {
        return BurritosToGoService(eventBus, burritosToGoApi, processScheduler, androidScheduler)
    }

    @Provides
    fun makeMainActivityImpl(mainActivityPresenter: MainActivityPresenter): MainActivityImpl {
        return MainActivityImpl(mainActivityPresenter)
    }

    @Provides
    fun makeMainActivityPresenter(eventBus: EventBus): MainActivityPresenter {
        return MainActivityPresenter(eventBus)
    }
}