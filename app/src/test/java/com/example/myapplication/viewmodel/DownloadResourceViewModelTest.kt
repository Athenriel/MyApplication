package com.example.myapplication.viewmodel

import android.content.Context
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.datasource.Resource
import com.example.myapplication.datasource.remote.RemoteDataSource
import com.example.myapplication.datasource.remote.ResourceError
import com.example.myapplication.datasource.remote.RetrofitFactory
import com.example.myapplication.datasource.remote.model.ProgressDownloadModel
import com.example.myapplication.datasource.remote.model.ResourceDownloadedModel
import com.example.myapplication.datasource.remote.model.RetrofitAndInterceptorModel
import com.example.myapplication.utils.Utils
import com.jraska.livedata.test
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

/**
 * Created by Athenriel on 30/03/2020.
 */
@ExperimentalCoroutinesApi
class DownloadResourceViewModelTest {

    private lateinit var downloadResourceViewModel: DownloadResourceViewModel
    private val remoteDataSourceMock = mockk<RemoteDataSource>()

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        downloadResourceViewModel = DownloadResourceViewModel(remoteDataSourceMock)
    }

    @Test
    fun downloadResourceSuccess() {
        val url = "https://developer.android.com/images/android-developers.png"
        val resourceId = "android-developers"
        val progressLiveData: MutableLiveData<ProgressDownloadModel> = MutableLiveData()
        val uriLiveData: MutableLiveData<Resource<ResourceDownloadedModel, ResourceError>> =
            MutableLiveData()
        val contextMock = mockk<Context>()
        mockkObject(RetrofitFactory)
        mockkObject(Utils)
        val retrofitAndInterceptorModelMock = mockk<RetrofitAndInterceptorModel>()
        val responseBodyMock = mockk<ResponseBody>()
        val response = Response.success(responseBodyMock)
        val uriMock = mockk<Uri>()
        progressLiveData.postValue(ProgressDownloadModel(10, resourceId))
        progressLiveData.test().awaitValue().assertHasValue()
            .assertValue { it.resourceId == resourceId && it.progressDownload == 10 }
        progressLiveData.postValue(ProgressDownloadModel(90, resourceId))
        progressLiveData.test().awaitValue().assertHasValue()
            .assertValue { it.resourceId == resourceId && it.progressDownload == 90 }
        coEvery {
            RetrofitFactory.createRetrofitAndInterceptorModel(
                any(),
                any()
            )
        } returns retrofitAndInterceptorModelMock
        coEvery { remoteDataSourceMock.getResource(any(), any()) } returns response
        coEvery { Utils.saveResourceToShareFolder(any(), any(), any(), any()) } returns uriMock
        downloadResourceViewModel.downloadResource(url, contextMock, progressLiveData, uriLiveData)
        uriLiveData.test().awaitValue().assertHasValue()
            .assertValue { it.data?.resourceId == resourceId && it.data?.uri == uriMock }
    }

    @Test
    fun downloadResourceUnknownError() {
        val url = "https://developer.android.com/images/android-developers.png"
        val progressLiveData: MutableLiveData<ProgressDownloadModel> = MutableLiveData()
        val uriLiveData: MutableLiveData<Resource<ResourceDownloadedModel, ResourceError>> =
            MutableLiveData()
        val contextMock = mockk<Context>()
        mockkObject(RetrofitFactory)
        mockkObject(Utils)
        val retrofitAndInterceptorModelMock = mockk<RetrofitAndInterceptorModel>()
        val responseBodyMock = mockk<ResponseBody>()
        val response = Response.success(responseBodyMock)
        coEvery {
            RetrofitFactory.createRetrofitAndInterceptorModel(
                any(),
                any()
            )
        } returns retrofitAndInterceptorModelMock
        coEvery { remoteDataSourceMock.getResource(any(), any()) } returns response
        coEvery { Utils.saveResourceToShareFolder(any(), any(), any(), any()) } returns null
        downloadResourceViewModel.downloadResource(url, contextMock, progressLiveData, uriLiveData)
        uriLiveData.test().awaitValue().assertHasValue()
            .assertValue { it.error == ResourceError.UNKNOWN }
    }

    @Test
    fun downloadResourceUnknownException() {
        val url = "https://developer.android.com/images/android-developers.png"
        val progressLiveData: MutableLiveData<ProgressDownloadModel> = MutableLiveData()
        val uriLiveData: MutableLiveData<Resource<ResourceDownloadedModel, ResourceError>> =
            MutableLiveData()
        val contextMock = mockk<Context>()
        mockkObject(RetrofitFactory)
        mockkObject(Utils)
        val retrofitAndInterceptorModelMock = mockk<RetrofitAndInterceptorModel>()
        val responseBodyMock = mockk<ResponseBody>()
        val response = Response.success(responseBodyMock)
        coEvery {
            RetrofitFactory.createRetrofitAndInterceptorModel(
                any(),
                any()
            )
        } returns retrofitAndInterceptorModelMock
        coEvery { remoteDataSourceMock.getResource(any(), any()) } returns response
        coEvery {
            Utils.saveResourceToShareFolder(
                any(),
                any(),
                any(),
                any()
            )
        } throws Exception("Exception")
        downloadResourceViewModel.downloadResource(url, contextMock, progressLiveData, uriLiveData)
        uriLiveData.test().awaitValue().assertHasValue()
            .assertValue { it.error == ResourceError.NETWORK }
    }

    @Test
    fun downloadResourceError() {
        val url = "https://developer.android.com/images/android-developers.png"
        val progressLiveData: MutableLiveData<ProgressDownloadModel> = MutableLiveData()
        val uriLiveData: MutableLiveData<Resource<ResourceDownloadedModel, ResourceError>> =
            MutableLiveData()
        val contextMock = mockk<Context>()
        mockkObject(RetrofitFactory)
        val retrofitAndInterceptorModelMock = mockk<RetrofitAndInterceptorModel>()
        val response =
            Response.error<ResponseBody>(401, "body".toResponseBody("media/type".toMediaType()))
        coEvery {
            RetrofitFactory.createRetrofitAndInterceptorModel(
                any(),
                any()
            )
        } returns retrofitAndInterceptorModelMock
        coEvery { remoteDataSourceMock.getResource(any(), any()) } returns response
        downloadResourceViewModel.downloadResource(url, contextMock, progressLiveData, uriLiveData)
        uriLiveData.test().awaitValue().assertHasValue()
            .assertValue { it.error == ResourceError.UNKNOWN }
    }

    @Test
    fun downloadResourceException() {
        val url = "https://developer.android.com/images/android-developers.png"
        val progressLiveData: MutableLiveData<ProgressDownloadModel> = MutableLiveData()
        val uriLiveData: MutableLiveData<Resource<ResourceDownloadedModel, ResourceError>> =
            MutableLiveData()
        val contextMock = mockk<Context>()
        mockkObject(RetrofitFactory)
        val retrofitAndInterceptorModelMock = mockk<RetrofitAndInterceptorModel>()
        coEvery {
            RetrofitFactory.createRetrofitAndInterceptorModel(
                any(),
                any()
            )
        } returns retrofitAndInterceptorModelMock
        coEvery { remoteDataSourceMock.getResource(any(), any()) } throws Exception("Exception")
        downloadResourceViewModel.downloadResource(url, contextMock, progressLiveData, uriLiveData)
        uriLiveData.test().awaitValue().assertHasValue()
            .assertValue { it.error == ResourceError.NETWORK }
    }

}
