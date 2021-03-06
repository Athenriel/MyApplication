package com.example.myapplication.worker

import android.content.Context
import androidx.work.*
import com.example.myapplication.datasource.local.LocalDataSource
import com.example.myapplication.datasource.remote.RemoteDataSource
import com.example.myapplication.model.UserLocationModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

/**
 * Created by Athenriel on 27/03/2020.
 */
class LocationUpdateWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params),
        KoinComponent {

    private val userLocationModel: UserLocationModel by inject()
    private val remoteDataSource: RemoteDataSource by inject()
    private val localDataSource: LocalDataSource by inject()

    companion object {
        fun enqueue(workManager: WorkManager) {
            val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            val workBuilder =
                    OneTimeWorkRequestBuilder<LocationUpdateWorker>().setConstraints(constraints)
                            .setBackoffCriteria(BackoffPolicy.LINEAR, 5, TimeUnit.MINUTES)
            workManager.enqueueUniqueWork(
                    LocationUpdateWorker::class.java.simpleName,
                    ExistingWorkPolicy.REPLACE, workBuilder.build()
            )
        }
    }

    override suspend fun doWork(): Result {
        return if (localDataSource.isSignedIn()) {
            try {
                val response = remoteDataSource.sendLocationToServer(
                        userLocationModel.latitude,
                        userLocationModel.longitude
                )
                if (response.isSuccessful && response.body()?.updated == true) {
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
