package com.example.myapplication.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        const val VIEW_USERS_EXTRA = "viewUsersExtra"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.getBooleanExtra(VIEW_USERS_EXTRA, false) &&
            findNavController(R.id.nav_host_fragment).currentDestination?.id == R.id.UsersHomeFragment
        ) {
            val directions =
                UsersHomeFragmentDirections.actionUsersHomeFragmentToViewUsersFragment(
                    true
                )
            findNavController(R.id.nav_host_fragment).navigate(directions)
        }
    }

}
