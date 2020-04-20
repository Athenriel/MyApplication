package com.example.myapplication.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentLocationBinding
import com.example.myapplication.model.UserLocationModel
import com.example.myapplication.ui.dialog.DarkBottomDialogFragment
import com.example.myapplication.utils.Utils
import org.koin.android.ext.android.inject

/**
 * Created by Athenriel on 20/04/2020.
 */
class LocationFragment : Fragment() {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!
    private val userLocationModel: UserLocationModel by inject()

    companion object {
        private const val SHARE_LOCATION_CODE = 503
        private const val PERMISSION_CODE = 5421
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentLocationShareBtn.setOnClickListener {
            Utils.shareLocation(
                userLocationModel.latitude,
                userLocationModel.longitude,
                getString(R.string.share_location_text),
                this,
                SHARE_LOCATION_CODE,
                object : Utils.ShareException {
                    override fun noAppFoundException() {
                        val title = getString(R.string.error_title)
                        val text =
                            getString(R.string.error_share_location_activity_resolve)
                        Utils.showSimpleDialog(context, title, text)
                    }
                })
        }
        (activity as MainActivity?)?.getLocationLiveData()?.observe(viewLifecycleOwner, Observer {
            setCoordsText()
        })
        setCoordsText()
        askPermission()
    }

    private fun setCoordsText() {
        val text =
            getString(R.string.coord_lat) + userLocationModel.latitude + getString(R.string.coord_lon) + userLocationModel.longitude
        binding.fragmentLocationCoordsTv.text = text
    }

    private fun askPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.let { activitySafe ->
                val permissionGranted = ContextCompat.checkSelfPermission(
                    activitySafe,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                if (!permissionGranted) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            activitySafe,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        val title = getString(R.string.explain_location_required_title)
                        val text = getString(R.string.explain_location_required_text)
                        AlertDialog.Builder(activitySafe, R.style.AlertDialogTheme)
                            .setTitle(title)
                            .setMessage(text)
                            .setPositiveButton(activitySafe.getString(R.string.ok)) { dialog, _ ->
                                val intent = Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                                )
                                activitySafe.startActivity(intent)
                                dialog.dismiss()
                            }
                            .show()
                    } else {
                        ActivityCompat.requestPermissions(
                            activitySafe,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_CODE
                        )
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SHARE_LOCATION_CODE) {
            val dialog = DarkBottomDialogFragment.newInstance(
                getString(R.string.location_shared_text),
                binding.fragmentLocationCoordsTv.text.toString()
            )
            dialog.show(
                childFragmentManager,
                DarkBottomDialogFragment::class.simpleName
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}