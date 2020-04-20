package com.example.myapplication.datasource.remote.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Athenriel on 20/04/2020.
 */
data class UpdateLocationRequest(
    @SerializedName("latitude")
    @Expose
    val latitude: Double,
    @SerializedName("longitude")
    @Expose
    val longitude: Double
)