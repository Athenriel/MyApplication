package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentShareBinding
import com.example.myapplication.datasource.Resource
import com.example.myapplication.datasource.remote.ResourceError
import com.example.myapplication.datasource.remote.model.ProgressDownloadModel
import com.example.myapplication.datasource.remote.model.ResourceDownloadedModel
import com.example.myapplication.utils.Utils
import com.example.myapplication.viewmodel.DownloadResourceViewModel
import org.koin.android.ext.android.inject
import timber.log.Timber

/**
 * Created by Athenriel on 26/03/2020.
 */
class ShareFragment : Fragment() {

    private var _binding: FragmentShareBinding? = null
    private val binding get() = _binding!!
    private val downloadResourceViewModel: DownloadResourceViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShareBinding.inflate(inflater, container, false)
        Utils.eraseShareFiles(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentShareUrlEt.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_GO -> {
                    val url = binding.fragmentShareUrlEt.text.toString()
                    if (url.isBlank()) {
                        Toast.makeText(context, R.string.share_url_empty_toast, Toast.LENGTH_LONG)
                            .show()
                        binding.fragmentShareUrlEt.setText("https://developer.android.com/images/android-developers.png")
                    } else {
                        Utils.hideKeyboard(context, binding.fragmentShareUrlEt)
                        binding.fragmentShareResourcePb.isVisible = true
                        binding.fragmentShareResourcePb.progress = 0
                        val progressLiveData: MutableLiveData<ProgressDownloadModel> =
                            MutableLiveData()
                        val uriLiveData: MutableLiveData<Resource<ResourceDownloadedModel, ResourceError>> =
                            MutableLiveData()
                        progressLiveData.observe(viewLifecycleOwner, Observer {
                            Timber.d(
                                "Downloading %s progress %s",
                                it.resourceId,
                                it.progressDownload
                            )
                            binding.fragmentShareResourcePb.progress = it.progressDownload
                        })
                        uriLiveData.observe(viewLifecycleOwner, Observer {
                            Timber.d(
                                "Uri from downloaded resource %s: %s",
                                it.data?.resourceId,
                                it.data?.uri
                            )
                            if (it.error == null) {
                                binding.fragmentShareResourcePb.isVisible = false
                                it.data?.uri?.let { uriSafe ->
                                    Utils.sharePhoto(
                                        uriSafe,
                                        this,
                                        2112,
                                        object : Utils.ShareException {
                                            override fun noAppFoundException() {
                                                val title = getString(R.string.error_title)
                                                val text =
                                                    getString(R.string.error_share_image_activity_resolve)
                                                Utils.showSimpleDialog(context, title, text)
                                            }
                                        })
                                }
                            } else {
                                val title = getString(R.string.error_title)
                                val text = getString(R.string.error_downloading_resource)
                                Utils.showSimpleDialog(context, title, text)
                            }
                        })
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
