package com.example.myapplication.worker

import android.content.Context
import androidx.work.*
import com.example.myapplication.datasource.local.LocalDataSource
import com.example.myapplication.datasource.remote.RemoteDataSource
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.concurrent.TimeUnit

/**
 * Created by Athenriel on 27/03/2020.
 */
class DeviceIdUpdateWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params),
    KoinComponent {

    private val remoteDataSource: RemoteDataSource by inject()
    private val localDataSource: LocalDataSource by inject()

    companion object {
        fun enqueue(workManager: WorkManager) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val workBuilder =
                OneTimeWorkRequestBuilder<DeviceIdUpdateWorker>().setConstraints(constraints)
                    .setBackoffCriteria(BackoffPolicy.LINEAR, 5, TimeUnit.MINUTES)
            workManager.enqueueUniqueWork(
                DeviceIdUpdateWorker::class.java.simpleName,
                ExistingWorkPolicy.REPLACE, workBuilder.build()
            )
        }
    }

    override suspend fun doWork(): Result {
        val deviceId = localDataSource.getDeviceIdToUpdate()
        return if (deviceId.isNotBlank() && localDataSource.isSignedIn()) {
            try {
                val response = remoteDataSource.sendDeviceIdToServer(deviceId)
                if (response.isSuccessful && response.body()?.updated == true) {
                    localDataSource.removeDeviceIdToUpdate()
                    Result.success()
                } else {
                    Result.retry()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.retry()
            }
        } else {
            Result.success()
        }
    }

}
