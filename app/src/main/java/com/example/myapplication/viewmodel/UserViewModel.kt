package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.datasource.Resource
import com.example.myapplication.datasource.local.LocalDataSource
import com.example.myapplication.datasource.local.PersistanceError
import com.example.myapplication.datasource.local.database.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        CoroutineScope(Dispatchers.IO).launch {
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
        CoroutineScope(Dispatchers.IO).launch {
            try {
                localDataSource.insertUser(UserEntity(0, firstName, lastName))
                withContext(Dispatchers.Main) {
                    data.postValue(Resource.success(true))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    e.printStackTrace()
                    data.postValue(Resource.error(PersistanceError.UNKNOWN, e.message))
                }
            }
        }
        return data
    }

    fun deleteUser(user: UserEntity): LiveData<Resource<Boolean, PersistanceError>> {
        val data = MutableLiveData<Resource<Boolean, PersistanceError>>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                localDataSource.deleteUser(user)
                withContext(Dispatchers.Main) {
                    data.postValue(Resource.success(true))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    e.printStackTrace()
                    data.postValue(Resource.error(PersistanceError.UNKNOWN, e.message))
                }
            }
        }
        return data
    }

}
