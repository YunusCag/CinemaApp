package com.yunuscagliyan.sinemalog.data.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object TheMovieDBClient {
    fun getClients():TheMovieDBInterface{
        val requestInterceptor= Interceptor{chain ->
            val url=chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()
            val request=chain.request()
                .newBuilder()
                .url(url)
                .build()
            return@Interceptor chain.proceed(request)

        }
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheMovieDBInterface::class.java)
    }
}