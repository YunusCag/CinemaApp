package com.yunuscagliyan.sinemalog.di

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.yunuscagliyan.sinemalog.data.api.API_KEY
import com.yunuscagliyan.sinemalog.data.api.BASE_URL
import com.yunuscagliyan.sinemalog.data.api.TheMovieDBInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class RetrofitModule {

    @Singleton
    @Provides
    fun provideHGsonBuilder():Gson{
        return GsonBuilder()
            .create()
    }

    @Singleton
    @Provides
    fun provideRequestInterceptor():Interceptor{
        return Interceptor{ chain ->
            val url=chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("api_key",API_KEY)
                .build()
            val request=chain.request()
                .newBuilder()
                .url(url)
                .build()
            return@Interceptor chain.proceed(request)
        }
    }
    @Singleton
    @Provides
    fun provideOkHttpClient(requestInterceptor: Interceptor):OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60,TimeUnit.SECONDS)
            .build()
    }
    @Singleton
    @Provides
    fun provideRetrofit(gson:Gson,okHttpClient: OkHttpClient):Retrofit.Builder{
        return  Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }
    @Singleton
    @Provides
    fun provideTheMovieDBInterface(retrofit: Retrofit.Builder):TheMovieDBInterface{
        return retrofit
            .build()
            .create(TheMovieDBInterface::class.java)

    }
}