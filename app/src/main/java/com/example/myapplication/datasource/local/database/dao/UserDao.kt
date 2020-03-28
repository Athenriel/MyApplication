package com.example.myapplication.datasource.local.database.dao

import androidx.room.*
import com.example.myapplication.datasource.local.database.entity.UserEntity

/**
 * Created by Athenriel on 25/03/2020.
 */
@Dao
interface UserDao {
    @Query("SELECT * FROM userentity")
    suspend fun getAll(): List<UserEntity>

    @Insert
    suspend fun insert(user: UserEntity)

    @Update
    suspend fun update(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("DELETE FROM userentity")
    suspend fun deleteAll()
}
