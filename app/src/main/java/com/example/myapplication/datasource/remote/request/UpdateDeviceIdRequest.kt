package com.example.myapplication.datasource.remote.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Athenriel on 27/03/2020.
 */
data class UpdateDeviceIdRequest(
    @SerializedName("deviceId")
    @Expose
    val deviceId: String
)
