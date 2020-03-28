package com.example.myapplication.datasource.remote

import com.example.myapplication.BuildConfig

/**
 * Created by Athenriel on 25/03/2020.
 */
object BaseUrlRetriever {

    fun getBaseUrl(): String {
        return if (BuildConfig.DEBUG) {
            "https://dev.url"
        } else {
            "https://prod.url"
        }
    }

}
