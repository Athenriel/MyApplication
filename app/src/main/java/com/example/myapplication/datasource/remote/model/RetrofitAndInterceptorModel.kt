package com.example.myapplication.datasource.remote.model

import com.example.myapplication.datasource.remote.interceptor.DownloadProgressInterceptor
import retrofit2.Retrofit

/**
 * Created by Athenriel on 25/03/2020.
 */
data class RetrofitAndInterceptorModel(val retrofit: Retrofit, val downloadProgressInterceptor: DownloadProgressInterceptor)
