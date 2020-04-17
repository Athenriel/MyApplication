package com.example.myapplication.ui

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        const val VIEW_USERS_EXTRA = "viewUsersExtra"
        private const val SPLASH_TIME = 1000L
    }

    private lateinit var binding: ActivityMainBinding

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
                else -> false
            }
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavBar.isVisible = when (destination.id) {
                R.id.CreateUserFragment -> true
                R.id.ViewUsersFragment -> true
                R.id.ShareFragment -> true
                else -> false
            }
        }
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

}
