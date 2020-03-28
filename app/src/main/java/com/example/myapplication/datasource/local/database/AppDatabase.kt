package com.example.myapplication.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.datasource.local.database.dao.UserDao
import com.example.myapplication.datasource.local.database.entity.UserEntity

/**
 * Created by Athenriel on 25/03/2020.
 */
@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
