package com.example.myapplication

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.room.Room
import androidx.work.WorkManager
import com.example.myapplication.datasource.local.LocalDataSource
import com.example.myapplication.datasource.local.database.AppDatabase
import com.example.myapplication.datasource.remote.RemoteDataSource
import com.example.myapplication.model.UserLocationModel
import com.example.myapplication.viewmodel.AccountViewModel
import com.example.myapplication.viewmodel.DownloadResourceViewModel
import com.example.myapplication.viewmodel.UserViewModel
import com.example.myapplication.worker.DeviceIdUpdateWorker
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

/**
 * Created by Athenriel on 25/03/2020.
 */
class MyApplication : Application() {

    companion object {
        private const val PREFERENCES_KEY = "myAppPrefs"
        private const val APP_DATABASE_NAME = "database"
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.d("Timber logging is active")
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, APP_DATABASE_NAME
        ).build()
        startKoin {
            printLogger()
            androidContext(this@MyApplication)
            modules(module {
                single { provideSharedPreferences(this@MyApplication) }
                single { RemoteDataSource() }
                single { LocalDataSource(db, get()) }
                single { UserLocationModel(0.0, 0.0) }
                viewModel { DownloadResourceViewModel(get()) }
                viewModel { UserViewModel(get()) }
                viewModel { AccountViewModel(get()) }
            })
        }
        FirebaseApp.initializeApp(this)
        createNotificationChannel()
        startDeviceIdUpdateWorker()
    }

    private fun provideSharedPreferences(app: Application): SharedPreferences =
        app.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)

    private fun startDeviceIdUpdateWorker() {
        DeviceIdUpdateWorker.enqueue(WorkManager.getInstance(this))
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val id = getString(R.string.default_notification_channel_id)
            val name = getString(R.string.default_notification_channel_name)
            val descriptionText = getString(R.string.default_notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(id, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}
