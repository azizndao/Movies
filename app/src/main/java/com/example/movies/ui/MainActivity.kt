package com.example.movies.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.movies.R
import com.example.movies.databinding.ActivityMainBinding
import com.example.movies.utils.extensions.hide
import com.example.movies.utils.extensions.show
import timber.log.Timber

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment).navController

        navController.addOnDestinationChangedListener(this)
        binding.navigationBar.setupWithNavController(navController)
    }

    private val bottomNavItems =
        listOf(R.id.nav_home, R.id.nav_movies, R.id.nav_tv_show, R.id.nav_search)

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        val bottomBar = binding.navigationBar
        try {
            if (destination.id in bottomNavItems) {
                bottomBar.show()
            } else {
                bottomBar.hide()
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}