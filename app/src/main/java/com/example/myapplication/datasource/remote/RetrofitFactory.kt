package com.example.myapplication.datasource.remote

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.datasource.remote.interceptor.DownloadProgressInterceptor
import com.example.myapplication.datasource.remote.model.ProgressDownloadModel
import com.example.myapplication.datasource.remote.model.RetrofitAndInterceptorModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Athenriel on 25/03/2020.
 */
object RetrofitFactory {

    fun getUnAuthRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient().newBuilder()
            .addInterceptor(interceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .build()
        val builder = Retrofit.Builder()
            .baseUrl(BaseUrlRetriever.getBaseUrl())
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
        return builder.build()
    }

    fun getAuthRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient().newBuilder()
            .addInterceptor(interceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .build()
        val builder = Retrofit.Builder()
            .baseUrl(BaseUrlRetriever.getBaseUrl())
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
        return builder.build()
    }

    fun createRetrofitAndInterceptorModel(progressLiveData: MutableLiveData<ProgressDownloadModel>?,
                                          resourceId: String?): RetrofitAndInterceptorModel {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.HEADERS
        val downloadInterceptor = DownloadProgressInterceptor(progressLiveData, resourceId)
        val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(interceptor)
            .addInterceptor(downloadInterceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .build()
        val customRetrofit = Retrofit.Builder()
            .baseUrl(BaseUrlRetriever.getBaseUrl())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return RetrofitAndInterceptorModel(customRetrofit, downloadInterceptor)
    }

}
