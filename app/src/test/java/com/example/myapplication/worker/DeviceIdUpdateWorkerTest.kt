package com.example.myapplication.worker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.example.myapplication.datasource.local.LocalDataSource
import com.example.myapplication.datasource.remote.RemoteDataSource
import com.example.myapplication.datasource.remote.response.UpdateDeviceIdResponse
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
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
class DeviceIdUpdateWorkerTest : KoinTest {

    private lateinit var deviceIdUpdateWorker: DeviceIdUpdateWorker
    private lateinit var remoteDataSourceMock: RemoteDataSource
    private lateinit var localDataSourceMock: LocalDataSource

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        remoteDataSourceMock = mockk()
        localDataSourceMock = mockk()
        startKoin {
            modules(module {
                single { remoteDataSourceMock }
                single { localDataSourceMock }
            })
        }
        deviceIdUpdateWorker =
            TestListenableWorkerBuilder<DeviceIdUpdateWorker>(ApplicationProvider.getApplicationContext()).build()
    }

    @After
    fun cleanUp() {
        stopKoin()
    }

    @Test
    fun noTokenToUpdate() {
        coEvery { localDataSourceMock.getDeviceIdToUpdate() } returns ""
        runBlocking {
            assert(deviceIdUpdateWorker.doWork() == ListenableWorker.Result.success())
        }
    }

    @Test
    fun userNotLoggedIn() {
        coEvery { localDataSourceMock.getDeviceIdToUpdate() } returns "token"
        coEvery { localDataSourceMock.isSignedIn() } returns false
        runBlocking {
            assert(deviceIdUpdateWorker.doWork() == ListenableWorker.Result.success())
        }
    }

    @Test
    fun tokenIsBlank() {
        coEvery { localDataSourceMock.getDeviceIdToUpdate() } returns " "
        coEvery { localDataSourceMock.isSignedIn() } returns true
        runBlocking {
            assert(deviceIdUpdateWorker.doWork() == ListenableWorker.Result.success())
        }
    }

    @Test
    fun updateTokenSuccess() {
        coEvery { localDataSourceMock.getDeviceIdToUpdate() } returns "token"
        coEvery { localDataSourceMock.isSignedIn() } returns true
        val updateResponse = mockk<UpdateDeviceIdResponse>()
        coEvery { updateResponse.updated } returns true
        val response = Response.success(updateResponse)
        coEvery { remoteDataSourceMock.sendDeviceIdToServer(any()) } returns response
        coEvery { localDataSourceMock.removeDeviceIdToUpdate() } just runs
        runBlocking {
            assert(deviceIdUpdateWorker.doWork() == ListenableWorker.Result.success())
        }
    }

    @Test
    fun updateTokenUnknownException() {
        coEvery { localDataSourceMock.getDeviceIdToUpdate() } returns "token"
        coEvery { localDataSourceMock.isSignedIn() } returns true
        val updateResponse = mockk<UpdateDeviceIdResponse>()
        coEvery { updateResponse.updated } returns true
        val response = Response.success(updateResponse)
        coEvery { remoteDataSourceMock.sendDeviceIdToServer(any()) } returns response
        coEvery { localDataSourceMock.removeDeviceIdToUpdate() } throws Exception("Exception")
        runBlocking {
            assert(deviceIdUpdateWorker.doWork() == ListenableWorker.Result.retry())
        }
    }

    @Test
    fun updateTokenNotUpdated() {
        coEvery { localDataSourceMock.getDeviceIdToUpdate() } returns "token"
        coEvery { localDataSourceMock.isSignedIn() } returns true
        val updateResponse = mockk<UpdateDeviceIdResponse>()
        coEvery { updateResponse.updated } returns false
        val response = Response.success(updateResponse)
        coEvery { remoteDataSourceMock.sendDeviceIdToServer(any()) } returns response
        runBlocking {
            assert(deviceIdUpdateWorker.doWork() == ListenableWorker.Result.retry())
        }
    }

    @Test
    fun updateTokenError() {
        coEvery { localDataSourceMock.getDeviceIdToUpdate() } returns "token"
        coEvery { localDataSourceMock.isSignedIn() } returns true
        val response = Response.error<UpdateDeviceIdResponse>(
            401,
            "body".toResponseBody("media/type".toMediaType())
        )
        coEvery { remoteDataSourceMock.sendDeviceIdToServer(any()) } returns response
        runBlocking {
            assert(deviceIdUpdateWorker.doWork() == ListenableWorker.Result.retry())
        }
    }

    @Test
    fun updateTokenException() {
        coEvery { localDataSourceMock.getDeviceIdToUpdate() } returns "token"
        coEvery { localDataSourceMock.isSignedIn() } returns true
        coEvery { remoteDataSourceMock.sendDeviceIdToServer(any()) } throws Exception("Exception")
        runBlocking {
            assert(deviceIdUpdateWorker.doWork() == ListenableWorker.Result.retry())
        }
    }

}
