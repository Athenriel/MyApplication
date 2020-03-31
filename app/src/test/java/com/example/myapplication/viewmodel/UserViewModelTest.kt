package com.example.myapplication.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.example.myapplication.datasource.Resource
import com.example.myapplication.datasource.local.LocalDataSource
import com.example.myapplication.datasource.local.PersistanceError
import com.example.myapplication.datasource.local.database.entity.UserEntity
import com.jraska.livedata.test
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Athenriel on 30/03/2020.
 */
@ExperimentalCoroutinesApi
class UserViewModelTest {

    private lateinit var userViewModel: UserViewModel
    private val localDataSourceMock = mockk<LocalDataSource>()

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        userViewModel = UserViewModel(localDataSourceMock)
    }

    @Test
    fun getUsersSuccess() {
        lateinit var liveDataResponse: LiveData<Resource<List<UserEntity>?, PersistanceError>>
        val userEntityMock = mockk<UserEntity>()
        val userEntityList = listOf(userEntityMock)
        coEvery { localDataSourceMock.getUsers() } returns userEntityList
        liveDataResponse = userViewModel.getUsers()
        liveDataResponse.test().awaitValue().assertHasValue()
            .assertValue { value -> value.error == null && value.data == userEntityList }
    }

    @Test
    fun getUsersException() {
        lateinit var liveDataResponse: LiveData<Resource<List<UserEntity>?, PersistanceError>>
        coEvery { localDataSourceMock.getUsers() } throws Exception("Exception")
        liveDataResponse = userViewModel.getUsers()
        liveDataResponse.test().awaitValue().assertHasValue()
            .assertValue { value -> value.error != null }
    }

    @Test
    fun regexTests() {
        //May differ to app implementation of regex
        val neo = "Thomas Anderson"
        val tron = "7r0n"
        val blank = " "
        assert(userViewModel.checkAlphabeticRegex(neo))
        assert(!userViewModel.checkAlphabeticRegex(tron))
        assert(userViewModel.checkAlphabeticRegex(blank))
    }

}
