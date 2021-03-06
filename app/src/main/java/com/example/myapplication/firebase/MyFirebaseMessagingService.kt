package com.example.myapplication.firebase

import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkManager
import com.example.myapplication.R
import com.example.myapplication.datasource.local.LocalDataSource
import com.example.myapplication.worker.DeviceIdUpdateWorker
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

/**
 * Created by Athenriel on 26/03/2020.
 */
class MyFirebaseMessagingService : FirebaseMessagingService(), KoinComponent {

    private val localDataSource: LocalDataSource by inject()

    override fun onNewToken(token: String) {
        Timber.d("Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        //check if message contains a data payload
        remoteMessage.data.isNotEmpty().let {
            Timber.i("Message data payload: ${remoteMessage.data}")
        }
        //check if message contains a notification payload
        remoteMessage.notification?.let {
            Timber.i("Message notification title: ${it.title}")
            Timber.i("Message notification body: ${it.body}")
            Timber.i("Channel id: ${it.channelId}")
            sendNotification(it.body!!, it.title!!)
        }
    }

    override fun onDeletedMessages() {
    }

    private fun sendRegistrationToServer(token: String) {
        localDataSource.saveDeviceIdToUpdate(token)
        DeviceIdUpdateWorker.enqueue(WorkManager.getInstance(this))
    }

    companion object {
        private var notificationId = 1
    }

    private fun sendNotification(messageBody: String, messageTitle: String) {
        val channelId = getString(R.string.default_notification_channel_id)
        val intent = Intent(this, applicationContext.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val ringNotificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notifyBuilder = NotificationCompat.Builder(this, channelId).apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle(messageTitle)
            setContentText(messageBody)
            setAutoCancel(true)
            setChannelId(channelId)
            setSound(ringNotificationSound)
            setContentIntent(pendingIntent)
        }
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId++, notifyBuilder.build())
        }
    }

}
