package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.datasource.Resource
import com.example.myapplication.datasource.local.LocalDataSource
import com.example.myapplication.datasource.local.PersistanceError
import com.example.myapplication.datasource.local.database.entity.UserEntity
import kotlinx.coroutines.*
import timber.log.Timber

/**
 * Created by Athenriel on 05/03/2021.
 */
class AccountViewModel(private val localDataSource: LocalDataSource) : ViewModel() {

    private var job: Job? = null

    fun getUsers(): LiveData<Resource<List<UserEntity>?, PersistanceError>> {
        val data = MutableLiveData<Resource<List<UserEntity>?, PersistanceError>>()
        if (job?.isCancelled == false) {
            job?.cancel()
        }
        job = viewModelScope.launch {
            try {
                val users = localDataSource.getUsers()
                withContext(Dispatchers.Main) {
                    data.postValue(Resource.success(users))
                }
            } catch (e: Exception) {
                Timber.e(e)
                data.postValue(
                    Resource.error(
                        PersistanceError.UNKNOWN, null
                    )
                )
            }
        }
        return data
    }

    fun clearJob() {
        job?.cancel()
    }

}
