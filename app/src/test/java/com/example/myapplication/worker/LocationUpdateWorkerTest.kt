package com.example.myapplication.worker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.example.myapplication.datasource.local.LocalDataSource
import com.example.myapplication.datasource.remote.RemoteDataSource
import com.example.myapplication.datasource.remote.response.UpdateLocationResponse
import com.example.myapplication.model.UserLocationModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import retrofit2.Response

/**
 * Created by Athenriel on 30/03/2020.
 */
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class LocationUpdateWorkerTest : KoinTest {

    private lateinit var locationUpdateWorker: LocationUpdateWorker
    private lateinit var remoteDataSourceMock: RemoteDataSource
    private lateinit var localDataSourceMock: LocalDataSource
    private lateinit var userLocationModel: UserLocationModel

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        remoteDataSourceMock = mockk()
        localDataSourceMock = mockk()
        userLocationModel = mockk()
        startKoin {
            modules(module {
                single { remoteDataSourceMock }
                single { localDataSourceMock }
                single { userLocationModel }
            })
        }
        locationUpdateWorker =
            TestListenableWorkerBuilder<LocationUpdateWorker>(ApplicationProvider.getApplicationContext()).build()
    }

    @After
    fun cleanUp() {
        stopKoin()
    }

    @Test
    fun userNotLoggedIn() {
        coEvery { localDataSourceMock.isSignedIn() } returns false
        runBlocking {
            assert(locationUpdateWorker.doWork() == ListenableWorker.Result.success())
        }
    }

    @Test
    fun updateLocationSuccess() {
        coEvery { localDataSourceMock.isSignedIn() } returns true
        coEvery { userLocationModel.latitude } returns 0.0
        coEvery { userLocationModel.longitude } returns 0.0
        val updateResponse = mockk<UpdateLocationResponse>()
        coEvery { updateResponse.updated } returns true
        val response = Response.success(updateResponse)
        coEvery { remoteDataSourceMock.sendLocationToServer(any(), any()) } returns response
        runBlocking {
            assert(locationUpdateWorker.doWork() == ListenableWorker.Result.success())
        }
    }

    @Test
    fun updateLocationNotUpdated() {
        coEvery { localDataSourceMock.isSignedIn() } returns true
        coEvery { userLocationModel.latitude } returns 0.0
        coEvery { userLocationModel.longitude } returns 0.0
        val updateResponse = mockk<UpdateLocationResponse>()
        coEvery { updateResponse.updated } returns false
        val response = Response.success(updateResponse)
        coEvery { remoteDataSourceMock.sendLocationToServer(any(), any()) } returns response
        runBlocking {
            assert(locationUpdateWorker.doWork() == ListenableWorker.Result.retry())
        }
    }

    @Test
    fun updateLocationError() {
        coEvery { localDataSourceMock.isSignedIn() } returns true
        coEvery { userLocationModel.latitude } returns 0.0
        coEvery { userLocationModel.longitude } returns 0.0
        val response = Response.error<UpdateLocationResponse>(
            401,
            "body".toResponseBody("media/type".toMediaType())
        )
        coEvery { remoteDataSourceMock.sendLocationToServer(any(), any()) } returns response
        runBlocking {
            assert(locationUpdateWorker.doWork() == ListenableWorker.Result.retry())
        }
    }

    @Test
    fun updateLocationException() {
        coEvery { localDataSourceMock.isSignedIn() } returns true
        coEvery { userLocationModel.latitude } returns 0.0
        coEvery { userLocationModel.longitude } returns 0.0
        coEvery {
            remoteDataSourceMock.sendLocationToServer(
                any(),
                any()
            )
        } throws Exception("Exception")
        runBlocking {
            assert(locationUpdateWorker.doWork() == ListenableWorker.Result.retry())
        }
    }

}
