package com.example.myapplication.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import okhttp3.ResponseBody
import okio.BufferedSink
import okio.buffer
import okio.sink
import java.io.File
import java.io.FileInputStream

/**
 * Created by Athenriel on 25/03/2020.
 */
fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

fun <T1 : Any, T2 : Any, T3 : Any, R : Any> safeLet(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    block: (T1, T2, T3) -> R?
): R? {
    return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}

object Utils {
    private const val SHARE_FOLDER = "share"
    private const val AUTHORITY = "com.example.myapplication.provider"
    private const val RESOURCE_PREFIX = "resource_"

    interface ShareException {
        fun noAppFoundException()
    }

    fun eraseShareFiles(context: Context?) {
        context?.let { safeContext ->
            try {
                safeContext.getExternalFilesDir(SHARE_FOLDER)?.deleteRecursively()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fileExists(context: Context?, resourceId: String, type: String): Uri? {
        context?.let { contextSafe ->
            contextSafe.getExternalFilesDir(SHARE_FOLDER)?.let { shareFolder ->
                val name = RESOURCE_PREFIX + resourceId + type
                val file = File(shareFolder, name)
                return if (file.exists() && file.length() > 0) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        FileProvider.getUriForFile(
                            contextSafe,
                            AUTHORITY,
                            file
                        )
                    } else {
                        Uri.fromFile(file)
                    }
                } else {
                    null
                }
            }
        }
        return null
    }

    fun saveResourceToShareFolder(
        context: Context?, response: ResponseBody?,
        resourceId: String?, type: String
    ): Uri? {
        context?.let { safeContext ->
            safeContext.getExternalFilesDir(SHARE_FOLDER)?.let { shareFolder ->
                val name = RESOURCE_PREFIX + resourceId + type
                val target = File(shareFolder, name)
                var sink: BufferedSink? = null
                try {
                    if (!target.exists() && !target.createNewFile()) {
                        return null
                    }
                    sink = target.sink().buffer()
                    response?.let {
                        sink.writeAll(it.source())
                        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            FileProvider.getUriForFile(
                                safeContext,
                                AUTHORITY,
                                target
                            )
                        } else {
                            Uri.fromFile(target)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    return null
                } finally {
                    sink?.close()
                }
            }
        }
        return null
    }

    fun transferFileFromProvider(context: Context?, uriOrigin: Uri?): Uri? {
        var uri: Uri? = null
        try {
            safeLet(context, uriOrigin) { safeContext, safeUri ->
                safeContext.getExternalFilesDir(SHARE_FOLDER)?.let { folder ->
                    var name = ""
                    safeContext.contentResolver.query(safeUri, null, null, null, null)?.use {
                        if (it.moveToFirst()) {
                            val columnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                            name = it.getString(columnIndex)
                        }
                    }
                    if (name.isNotBlank()) {
                        safeContext.contentResolver.openFileDescriptor(safeUri, "r", null)
                            ?.let { fileDescriptor ->
                                val target =
                                    File(folder, "share_image_$name")
                                FileInputStream(fileDescriptor.fileDescriptor).copyTo(target.outputStream())
                                uri = FileProvider.getUriForFile(
                                    safeContext,
                                    AUTHORITY,
                                    target
                                )
                            }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return uri
    }

    fun getFileUriFromPathTransferIfNeeded(context: Context?, filePath: String?): Uri? {
        var uri: Uri? = null
        try {
            safeLet(context, filePath) { safeContext, safeFilePath ->
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    safeContext.getExternalFilesDir(SHARE_FOLDER)?.let {
                        val file = File(safeFilePath)
                        if (file.exists()) {
                            val type = safeFilePath.substringAfterLast(".")
                            val target =
                                File(it, "share_image_" + System.currentTimeMillis() + "." + type)
                            file.inputStream().copyTo(target.outputStream())
                            uri = FileProvider.getUriForFile(
                                safeContext,
                                AUTHORITY,
                                target
                            )
                        }
                    }
                } else {
                    val file = File(safeFilePath)
                    uri = if (file.exists()) {
                        Uri.fromFile(file)
                    } else {
                        null
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return uri
    }

    fun copyToClipboard(context: Context?, content: String, label: String, showToast: Boolean) {
        context?.let { safeContext ->
            val clipboard =
                safeContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText(label, content)
            clipboard?.setPrimaryClip(clip)
            if (showToast) {
                Toast.makeText(
                    safeContext,
                    safeContext.getString(R.string.toast_copied_clipboard),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun sharePhoto(
        photoUri: Uri,
        title: String,
        fragment: Fragment,
        requestCode: Int,
        shareException: ShareException?
    ) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, photoUri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val list = fragment.context?.packageManager?.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )
        if (!list.isNullOrEmpty()) {
            fragment.startActivityForResult(Intent.createChooser(intent, title), requestCode)
        } else {
            shareException?.noAppFoundException()
        }
    }

    fun shareLocation(
        latitude: Double,
        longitude: Double,
        title: String,
        fragment: Fragment,
        requestCode: Int,
        shareException: ShareException?
    ) {
        val text = "https://maps.google.com/maps?q=loc:$latitude,$longitude"
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        val list = fragment.context?.packageManager?.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )
        if (!list.isNullOrEmpty()) {
            fragment.startActivityForResult(Intent.createChooser(intent, title), requestCode)
        } else {
            shareException?.noAppFoundException()
        }
    }

    fun showSimpleDialog(context: Context?, title: String?, text: String?) {
        context?.let { safeContext ->
            AlertDialog.Builder(safeContext, R.style.AlertDialogTheme)
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton(safeContext.getString(R.string.ok)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    fun hideKeyboard(context: Context?, view: View?) {
        safeLet(context, view) { contextSafe, viewSafe ->
            val imm =
                contextSafe.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(viewSafe.windowToken, 0)
        }
    }

    fun saveDataInPreferences(key: String, data: Any?, sharedPreferences: SharedPreferences) {
        data?.let { dataSafe ->
            when (dataSafe) {
                is String -> sharedPreferences.edit { putString(key, dataSafe) }
                is Boolean -> sharedPreferences.edit { putBoolean(key, dataSafe) }
                is Int -> sharedPreferences.edit { putInt(key, dataSafe) }
                is Long -> sharedPreferences.edit { putLong(key, dataSafe) }
                is Float -> sharedPreferences.edit { putFloat(key, dataSafe) }
            }
        }
    }

    fun saveStringSetInPreferences(
        key: String,
        set: Set<String>,
        sharedPreferences: SharedPreferences
    ) {
        sharedPreferences.edit { putStringSet(key, set) }
    }

    fun getDataFromPreferences(
        key: String,
        default: Any,
        sharedPreferences: SharedPreferences
    ): Any {
        return when (default) {
            is String -> sharedPreferences.getString(key, default) ?: default
            is Boolean -> sharedPreferences.getBoolean(key, default)
            is Int -> sharedPreferences.getInt(key, default)
            is Long -> sharedPreferences.getLong(key, default)
            is Float -> sharedPreferences.getFloat(key, default)
            else -> default
        }
    }

    fun getStringSetFromPreferences(
        key: String,
        default: Set<String>,
        sharedPreferences: SharedPreferences
    ): Set<String> {
        return sharedPreferences.getStringSet(key, default) ?: default
    }

    fun removeKeyFromPreferences(key: String, sharedPreferences: SharedPreferences) {
        sharedPreferences.edit { remove(key) }
    }

}
