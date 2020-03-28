package com.example.myapplication.datasource.local

import android.content.SharedPreferences
import com.example.myapplication.datasource.local.database.AppDatabase
import com.example.myapplication.datasource.local.database.entity.UserEntity
import com.example.myapplication.utils.Utils

/**
 * Created by Athenriel on 25/03/2020.
 */
class LocalDataSource(private val db: AppDatabase, private val preferences: SharedPreferences) {

    companion object {
        private const val DEVICE_ID_KEY = "deviceIdKey"
    }

    suspend fun getUsers(): List<UserEntity> {
        return db.userDao().getAll()
    }

    suspend fun insertUser(user: UserEntity) {
        db.userDao().insert(user)
    }

    suspend fun updateUser(user: UserEntity) {
        db.userDao().update(user)
    }

    suspend fun deleteUser(user: UserEntity) {
        db.userDao().delete(user)
    }

    suspend fun deleteAllUsers() {
        db.userDao().deleteAll()
    }

    fun isSignedIn(): Boolean {
        return true
    }

    fun saveDeviceIdToUpdate(deviceId: String) {
        Utils.saveDataInPreferences(DEVICE_ID_KEY, deviceId, preferences)
    }

    fun getDeviceIdToUpdate(): String {
        return Utils.getDataFromPreferences(DEVICE_ID_KEY, "", preferences) as String
    }

    fun removeDeviceIdToUpdate() {
        Utils.removeKeyFromPreferences(DEVICE_ID_KEY, preferences)
    }

}

enum class PersistanceError { UNKNOWN, UNAUTHORIZED }
