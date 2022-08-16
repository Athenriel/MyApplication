package com.example.myapplication.ui.cuenta

import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentCreateUserBinding
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.ui.MainActivity
import com.example.myapplication.utils.Utils
import com.example.myapplication.viewmodel.UserViewModel
import org.koin.android.ext.android.inject

/**
 * Created by Athenriel on 26/03/2020.
 */
class CreateUserFragment :
    BaseFragment<FragmentCreateUserBinding>(FragmentCreateUserBinding::inflate) {

    private val userViewModel: UserViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentCreateUserCreateUserBtn.setOnClickListener {
            val firstName = binding.fragmentCreateUserFirstNameEt.text.toString().trim()
            val lastName = binding.fragmentCreateUserLastNameEt.text.toString().trim()
            if (checkName(firstName, true) && checkName(lastName, false)) {
                userViewModel.createUser(firstName, lastName)
                    .observe(viewLifecycleOwner) { userCreatedResource ->
                        if (userCreatedResource.error == null && userCreatedResource.data == true) {
                            sendSuccessNotification()
                            binding.fragmentCreateUserFirstNameEt.setText("")
                            binding.fragmentCreateUserLastNameEt.setText("")
                        } else {
                            val title = getString(R.string.error_title)
                            val text = getString(R.string.error_creating_user)
                            Utils.showSimpleDialog(this@CreateUserFragment.context, title, text)
                        }
                    }
            }
        }
        binding.fragmentCreateUserCuentaBtn.setOnClickListener {
            val directions =
                CreateUserFragmentDirections.actionCreateUserFragmentToCuentaFragment("ID3456787654")
            findNavController().navigate(directions)
        }
    }

    private fun checkName(name: String, isFirstName: Boolean): Boolean {
        if (name.isBlank()) {
            val title = getString(R.string.error_title)
            val text = if (isFirstName) {
                getString(R.string.first_name_text) + getString(R.string.error_name_empty)
            } else {
                getString(R.string.last_name_text) + getString(R.string.error_name_empty)
            }
            Utils.showSimpleDialog(context, title, text)
            return false
        } else if (!userViewModel.checkAlphabeticRegex(name)) {
            val title = getString(R.string.error_title)
            val text = if (isFirstName) {
                getString(R.string.first_name_text) + getString(R.string.error_name_regex)
            } else {
                getString(R.string.last_name_text) + getString(R.string.error_name_regex)
            }
            Utils.showSimpleDialog(context, title, text)
            return false
        } else {
            return true
        }
    }

    private fun sendSuccessNotification() {
        context?.let { safeContext ->
            val channelId = getString(R.string.default_notification_channel_id)
            val title = safeContext.getString(R.string.app_name)
            val body = safeContext.getString(R.string.create_user_success)
            val ringNotificationSound =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val vibration = arrayOf(100L, 400L, 300L, 700L)
            val intent = Intent(safeContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(MainActivity.VIEW_USERS_EXTRA, true)
            val pendingIntent =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.getActivity(safeContext, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
                } else {
                    PendingIntent.getActivity(safeContext, 0, intent, PendingIntent.FLAG_ONE_SHOT)
                }
            val notifyBuilder = NotificationCompat.Builder(safeContext, channelId).apply {
                setSmallIcon(R.mipmap.ic_launcher)
                setContentTitle(title)
                setContentText(body)
                setAutoCancel(true)
                setVibrate(vibration.toLongArray())
                setChannelId(channelId)
                setSound(ringNotificationSound)
                setContentIntent(pendingIntent)
            }
            with(NotificationManagerCompat.from(safeContext)) {
                notify(0, notifyBuilder.build())
            }
        }
    }

}
