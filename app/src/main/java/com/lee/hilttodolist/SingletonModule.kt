package com.lee.hilttodolist

import android.util.Log
import com.lee.hilttodolist.Utils.API
import com.lee.hilttodolist.Utils.Constants.TAG
import com.lee.hilttodolist.Utils.isJsonArray
import com.lee.hilttodolist.Utils.isJsonObject
import com.lee.hilttodolist.Interface.MainApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Singleton
    @Provides // 자동으로 주입? 제공해주는 것
    fun provideOkHttpClient(
        baseInterceptor: Interceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d(TAG, "RetrofitClient - 로그를 찍기 위해 로깅 인터셉터를 추가 / message : $message")

            when {
                message.isJsonObject() -> Log.d(TAG, JSONObject(message).toString(4))
                message.isJsonArray() -> Log.d(TAG, JSONArray(message).toString(4))
                else -> {
                    try {
                        Log.d(TAG, JSONObject(message).toString(4))
                    } catch (e: Exception) {
                        Log.d(TAG, message)
                    }
                }
            }
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(baseInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Singleton
    @Provides
    fun provideBaseInterceptor(): Interceptor =
        (Interceptor { chain ->
            Log.d(TAG, "RetrofitClient - intercept() called / 기본 파라메터 인터셉터 설정")

            // 오리지날 리퀘스터
            val originalRequest = chain.request()

//             헤더 추가
            val apiHeaderAdded = originalRequest.newBuilder()
                .addHeader("accept", "application/json")
                .addHeader("Content-Type", "multipart/form-data")
                .build()

            val finalRequest = apiHeaderAdded.newBuilder()
                .method(apiHeaderAdded.method, apiHeaderAdded.body).build()
            chain.proceed(finalRequest)
        })

    @Singleton
    @Provides
    fun provideRetrofitClient() : Retrofit.Builder = Retrofit.Builder()
        .baseUrl("https://phplaravel-574671-2962113.cloudwaysapps.com/api/v1/")
        // 서버로부터 데이터를 받아와서 원하는 타입으로 데이터를 바꾸기 위해
        // addConverterFactory(GsonConverterFactory.create())를 사용한다.
        .addConverterFactory(GsonConverterFactory.create())

    @Singleton
    @Provides
    fun provideMainApiService(okHttpClient: OkHttpClient, retrofit: Retrofit.Builder): MainApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(MainApiService::class.java)
}
