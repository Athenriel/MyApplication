package com.example.myapplication.datasource.remote

import com.example.myapplication.datasource.remote.model.RetrofitAndInterceptorModel
import com.example.myapplication.datasource.remote.request.UpdateDeviceIdRequest
import com.example.myapplication.datasource.remote.request.UpdateLocationRequest
import com.example.myapplication.datasource.remote.response.UpdateDeviceIdResponse
import com.example.myapplication.datasource.remote.response.UpdateLocationResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*

/**
 * Created by Athenriel on 25/03/2020.
 */
class RemoteDataSource {

    private val retrofitUnAuth: Retrofit =
        RetrofitFactory.getUnAuthRetrofit()

    private val retrofitAuth: Retrofit =
        RetrofitFactory.getAuthRetrofit()

    suspend fun sendDeviceIdToServer(deviceId: String): Response<UpdateDeviceIdResponse> {
        val request = UpdateDeviceIdRequest(deviceId)
        return retrofitAuth.create(ProfileClient::class.java).updateDeviceId(request)
    }

    suspend fun sendLocationToServer(
        latitude: Double,
        longitude: Double
    ): Response<UpdateLocationResponse> {
        val request = UpdateLocationRequest(latitude, longitude)
        return retrofitAuth.create(ProfileClient::class.java).updateLocation(request)
    }

    suspend fun getResource(
        url: String,
        retrofitAndInterceptorModel: RetrofitAndInterceptorModel
    ): Response<ResponseBody> {
        return retrofitAndInterceptorModel.retrofit.create(DownloadClient::class.java)
            .getResource(url)
    }

}

enum class ResourceError { UNKNOWN, UNAUTHENTICATED, NETWORK }

interface ProfileClient {

    @Headers("Content-Type:application/zip")
    @POST("profile/deviceId")
    suspend fun updateDeviceId(@Body request: UpdateDeviceIdRequest): Response<UpdateDeviceIdResponse>

    @Headers("Content-Type:application/zip")
    @POST("profile/location")
    suspend fun updateLocation(@Body request: UpdateLocationRequest): Response<UpdateLocationResponse>

}

interface DownloadClient {

    @Streaming
    @Headers("Content-Type:application/zip")
    @GET
    suspend fun getResource(@Url urlDownload: String?): Response<ResponseBody>

}
