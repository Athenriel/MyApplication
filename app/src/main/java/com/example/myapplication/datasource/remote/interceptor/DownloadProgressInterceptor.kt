package com.example.myapplication.datasource.remote.interceptor

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.datasource.remote.model.ProgressDownloadModel
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by Athenriel on 25/03/2020.
 */
class DownloadProgressInterceptor(private val progressLiveData: MutableLiveData<ProgressDownloadModel>?, private val resourceId: String?) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return progressLiveData?.let {
            val originalResponse = chain.proceed(chain.request())
            val sourceProgressStream = SourceProgressStream(originalResponse.body, progressLiveData, resourceId)
            originalResponse
                .newBuilder()
                .body(sourceProgressStream)
                .build()
        } ?: run {
            chain.proceed(chain.request())
        }
    }

}
