package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.datasource.Resource
import com.example.myapplication.datasource.local.LocalDataSource
import com.example.myapplication.datasource.local.PersistanceError
import com.example.myapplication.datasource.local.database.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.regex.Pattern

/**
 * Created by Athenriel on 26/03/2020.
 */
class UserViewModel(private val localDataSource: LocalDataSource) : ViewModel() {

    companion object {
        private const val ALPHABETIC_REGEX = """^[\w\s]+$"""
        private const val NO_DIGITS_REGEX = """^[\D]+$"""
    }

    fun getUsers(): LiveData<Resource<List<UserEntity>?, PersistanceError>> {
        val data = MutableLiveData<Resource<List<UserEntity>?, PersistanceError>>()
        viewModelScope.launch {
            try {
                val users = localDataSource.getUsers()
                data.postValue(Resource.success(users))
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

    fun checkAlphabeticRegex(name: String): Boolean {
        val patternNoDigits = Pattern.compile(NO_DIGITS_REGEX)
        val patternLetters = Pattern.compile(ALPHABETIC_REGEX)
        return patternNoDigits.matcher(name).matches() && patternLetters.matcher(name).matches()
    }

    fun createUser(
        firstName: String,
        lastName: String
    ): LiveData<Resource<Boolean, PersistanceError>> {
        val data = MutableLiveData<Resource<Boolean, PersistanceError>>()
        viewModelScope.launch {
            try {
                localDataSource.insertUser(UserEntity(0, firstName, lastName))
                data.postValue(Resource.success(true))
            } catch (e: Exception) {
                Timber.e(e)
                data.postValue(Resource.error(PersistanceError.UNKNOWN, e.message))
            }
        }
        return data
    }

    fun deleteUser(user: UserEntity): LiveData<Resource<Boolean, PersistanceError>> {
        val data = MutableLiveData<Resource<Boolean, PersistanceError>>()
        viewModelScope.launch {
            try {
                localDataSource.deleteUser(user)
                data.postValue(Resource.success(true))
            } catch (e: Exception) {
                Timber.e(e)
                data.postValue(Resource.error(PersistanceError.UNKNOWN, e.message))
            }
        }
        return data
    }

}
