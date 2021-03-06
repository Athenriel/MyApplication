package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.datasource.Resource
import com.example.myapplication.datasource.local.LocalDataSource
import com.example.myapplication.datasource.local.PersistanceError
import com.example.myapplication.datasource.local.database.entity.UserEntity
import kotlinx.coroutines.*

/**
 * Created by Francisco Bartilotti on 05/03/2021.
 */
class CuentaViewModel(private val localDataSource: LocalDataSource) : ViewModel() {

    private var job: Job? = null

    fun getUsers(): LiveData<Resource<List<UserEntity>?, PersistanceError>> {
        val data = MutableLiveData<Resource<List<UserEntity>?, PersistanceError>>()
        if (job?.isCancelled == false) {
            job?.cancel()
        }
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val users = localDataSource.getUsers()
                withContext(Dispatchers.Main) {
                    data.postValue(Resource.success(users))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    e.printStackTrace()
                    data.postValue(
                            Resource.error(
                                    PersistanceError.UNKNOWN, null
                            )
                    )
                }
            }
        }
        return data
    }

    fun clearJob() {
        job?.cancel()
    }

}
