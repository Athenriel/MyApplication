package com.example.myapplication.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.WorkManager
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.UserLocationModel
import com.example.myapplication.worker.LocationUpdateWorker
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    companion object {
        const val VIEW_USERS_EXTRA = "viewUsersExtra"
        private const val SPLASH_TIME = 1000L
        private const val LOC_INTERVAL = 60000L
        private const val FAST_LOC_INTERVAL = 30000L
        private const val RESOLVABLE_CODE = 1984
    }

    private lateinit var binding: ActivityMainBinding
    private val userLocationModel: UserLocationModel by inject()
    private var asked = false
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private lateinit var locationLiveData: MutableLiveData<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavBar.setupWithNavController(navController)
        binding.bottomNavBar.setOnNavigationItemSelectedListener {
            val clearNavOptions =
                NavOptions.Builder().setLaunchSingleTop(true).setPopUpTo(R.id.nav_graph, true)
                    .build()
            when (it.itemId) {
                R.id.menu_create_user_nav -> {
                    navController.navigate(R.id.CreateUserFragment, null, clearNavOptions)
                    true
                }
                R.id.menu_view_user_nav -> {
                    navController.navigate(R.id.ViewUsersFragment, null, clearNavOptions)
                    true
                }
                R.id.menu_share_nav -> {
                    navController.navigate(R.id.ShareFragment, null, clearNavOptions)
                    true
                }
                R.id.menu_location_nav -> {
                    navController.navigate(R.id.LocationFragment, null, clearNavOptions)
                    true
                }
                else -> false
            }
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavBar.isVisible = when (destination.id) {
                R.id.CreateUserFragment -> true
                R.id.ViewUsersFragment -> true
                R.id.ShareFragment -> true
                R.id.LocationFragment -> true
                else -> false
            }
        }
        locationLiveData = MutableLiveData()
        Handler().postDelayed({
            if (navController.currentDestination?.id == R.id.SplashFragment) {
                val directions = if (intent.getBooleanExtra(VIEW_USERS_EXTRA, false)) {
                    SplashFragmentDirections.actionSplashFragmentToViewUsersFragment(true)
                } else {
                    SplashFragmentDirections.actionSplashFragmentToCreateUserFragment()
                }
                navController.navigate(directions)
            }
        }, SPLASH_TIME)
    }

    override fun onResume() {
        super.onResume()
        trackLocation()
    }

    override fun onPause() {
        super.onPause()
        stopTracking()
    }

    fun getLocationLiveData(): LiveData<Boolean> {
        return locationLiveData
    }

    private fun trackLocation() {
        try {
            val permissionGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            if (permissionGranted) {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                val settingsClient = LocationServices.getSettingsClient(this)
                val locationRequest = LocationRequest()
                locationRequest.interval = LOC_INTERVAL
                locationRequest.fastestInterval = FAST_LOC_INTERVAL
                locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
                val settingsRequest =
                    LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()
                settingsClient.checkLocationSettings(settingsRequest).addOnSuccessListener {
                    fusedLocationProviderClient?.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.myLooper()
                    )
                }.addOnFailureListener {
                    if (it is ResolvableApiException && !asked) {
                        asked = true
                        it.startResolutionForResult(this, RESOLVABLE_CODE)
                    } else {
                        it.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopTracking() {
        fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            result?.let {
                userLocationModel.latitude = it.lastLocation.latitude
                userLocationModel.longitude = it.lastLocation.longitude
                locationLiveData.postValue(true)
                LocationUpdateWorker.enqueue(WorkManager.getInstance(this@MainActivity))
            }
        }
    }

}
