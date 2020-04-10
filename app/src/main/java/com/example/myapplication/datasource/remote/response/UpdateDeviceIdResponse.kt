package com.example.myapplication.datasource.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Athenriel on 27/03/2020.
 */
data class UpdateDeviceIdResponse(
    @SerializedName("updated")
    @Expose
    val updated: Boolean
)
