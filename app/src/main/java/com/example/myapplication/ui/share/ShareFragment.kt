package com.example.myapplication.ui.share

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentShareBinding
import com.example.myapplication.datasource.Resource
import com.example.myapplication.datasource.remote.ResourceError
import com.example.myapplication.datasource.remote.model.ProgressDownloadModel
import com.example.myapplication.datasource.remote.model.ResourceDownloadedModel
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.ui.dialog.BottomDialogListener
import com.example.myapplication.ui.dialog.DarkBottomDialogFragment
import com.example.myapplication.utils.Utils
import com.example.myapplication.viewmodel.DownloadResourceViewModel
import org.koin.android.ext.android.inject
import timber.log.Timber

/**
 * Created by Athenriel on 26/03/2020.
 */
class ShareFragment : BaseFragment<FragmentShareBinding>(FragmentShareBinding::inflate) {

    private val downloadResourceViewModel: DownloadResourceViewModel by inject()
    private var uri: Uri? = null

    companion object {
        private const val PICK_CODE = 117
        private const val SHARE_CODE = 2112
        private const val TEST_IMAGE_URL =
            "https://developer.android.com/images/android-developers.png"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Utils.eraseShareFiles(context)
        binding.fragmentShareImageIv.setOnClickListener {
            sharePhoto()
        }
        binding.fragmentSharePickBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }
            startActivityForResult(intent, PICK_CODE)
        }
        binding.fragmentShareUrlEt.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_GO -> {
                    val url = binding.fragmentShareUrlEt.text.toString()
                    if (url.isBlank()) {
                        Toast.makeText(context, R.string.share_url_empty_toast, Toast.LENGTH_LONG)
                            .show()
                        binding.fragmentShareUrlEt.setText(TEST_IMAGE_URL)
                    } else {
                        Utils.hideKeyboard(context, binding.fragmentShareUrlEt)
                        binding.fragmentShareResourcePb.isVisible = true
                        binding.fragmentShareResourcePb.progress = 0
                        val progressLiveData: MutableLiveData<ProgressDownloadModel> =
                            MutableLiveData()
                        val uriLiveData: MutableLiveData<Resource<ResourceDownloadedModel, ResourceError>> =
                            MutableLiveData()
                        progressLiveData.observe(viewLifecycleOwner) {
                            Timber.d(
                                "Downloading %s progress %s",
                                it.resourceId,
                                it.progressDownload
                            )
                            binding.fragmentShareResourcePb.progress = it.progressDownload
                        }
                        uriLiveData.observe(viewLifecycleOwner) {
                            Timber.d(
                                "Uri from downloaded resource %s: %s",
                                it.data?.resourceId,
                                it.data?.uri
                            )
                            Timber.d("Resource is %s", it)
                            if (it.error == null) {
                                binding.fragmentShareResourcePb.isVisible = false
                                uri = it.data?.uri
                                Glide.with(binding.fragmentShareImageIv)
                                    .load(uri)
                                    .fitCenter()
                                    .centerCrop()
                                    .into(binding.fragmentShareImageIv)
                            } else {
                                val title = getString(R.string.error_title)
                                val text = getString(R.string.error_downloading_resource)
                                Utils.showSimpleDialog(context, title, text)
                            }
                        }
                        downloadResourceViewModel.downloadResource(
                            url,
                            context,
                            progressLiveData,
                            uriLiveData
                        )
                    }
                    true
                }
                else -> false
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SHARE_CODE) {
            val dialog = DarkBottomDialogFragment.newInstance(
                getString(R.string.image_shared_text),
                uri?.lastPathSegment
            )
            dialog.bottomDialogListener = object : BottomDialogListener {
                override fun onDismiss() {
                    Glide.with(binding.fragmentShareImageIv).clear(binding.fragmentShareImageIv)
                    uri = null
                }
            }
            dialog.show(
                childFragmentManager,
                DarkBottomDialogFragment::class.simpleName
            )
        } else if (requestCode == PICK_CODE && resultCode == Activity.RESULT_OK) {
            uri = Utils.transferFileFromProvider(context, data?.data)
            sharePhoto()
        }
    }

    private fun sharePhoto() {
        uri?.let { uriSafe ->
            Utils.sharePhoto(
                uriSafe,
                getString(R.string.share_image_text),
                this,
                SHARE_CODE,
                object : Utils.ShareException {
                    override fun noAppFoundException() {
                        val title = getString(R.string.error_title)
                        val text =
                            getString(R.string.error_share_image_activity_resolve)
                        Utils.showSimpleDialog(context, title, text)
                    }
                })
        }
    }

}
