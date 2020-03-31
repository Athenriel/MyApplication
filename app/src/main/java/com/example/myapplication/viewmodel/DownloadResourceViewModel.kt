package com.example.myapplication.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.datasource.Resource
import com.example.myapplication.datasource.remote.RemoteDataSource
import com.example.myapplication.datasource.remote.ResourceError
import com.example.myapplication.datasource.remote.RetrofitFactory
import com.example.myapplication.datasource.remote.model.ProgressDownloadModel
import com.example.myapplication.datasource.remote.model.ResourceDownloadedModel
import com.example.myapplication.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Athenriel on 25/03/2020.
 */
class DownloadResourceViewModel(private val remoteDataSource: RemoteDataSource) : ViewModel() {

    fun downloadResource(
        url: String, context: Context?,
        progressLiveData: MutableLiveData<ProgressDownloadModel>,
        uriLiveData: MutableLiveData<Resource<ResourceDownloadedModel, ResourceError>>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val type = "." + url.substringAfterLast(".")
                val id = url.substringAfterLast("/").substringBeforeLast(".")
                val model = RetrofitFactory.createRetrofitAndInterceptorModel(progressLiveData, id)
                val response = remoteDataSource.getResource(url, model)
                if (response.isSuccessful) {
                    val uri =
                        Utils.saveResourceToShareFolder(context, response.body(), id, type)
                    withContext(Dispatchers.Main) {
                        uri?.let {
                            uriLiveData.postValue(
                                Resource.success(
                                    ResourceDownloadedModel(
                                        id,
                                        uri
                                    )
                                )
                            )
                        } ?: run {
                            uriLiveData.postValue(
                                Resource.error(
                                    ResourceError.UNKNOWN, ResourceDownloadedModel(null, null)
                                )
                            )
                        }
                    }
                } else {
                    uriLiveData.postValue(
                        Resource.error(
                            ResourceError.UNKNOWN, ResourceDownloadedModel(null, null)
                        )
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    e.printStackTrace()
                    uriLiveData.postValue(
                        Resource.error(
                            ResourceError.NETWORK, ResourceDownloadedModel(null, null)
                        )
                    )
                }
            }
        }
    }

}
