package com.giraffe.triplemapplication

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.giraffe.triplemapplication.databinding.ActivityMainBinding
import com.giraffe.triplemapplication.utils.LocalHelper
import com.giraffe.triplemapplication.utils.gone
import com.giraffe.triplemapplication.utils.show


class MainActivity : AppCompatActivity(), OnActivityCallback {
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalHelper.setLocale(this, LocalHelper.getLanguage(this))
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(binding.bottomNavView, navController)
        navController.addOnDestinationChangedListener { _: NavController?, navDestination: NavDestination, bundle: Bundle? ->
            if (navDestination.id == R.id.homeFragment || navDestination.id == R.id.searchResultFragment || navDestination.id == R.id.cartFragment || navDestination.id == R.id.profileFragment) {
                binding.bottomNavView.show()
            } else {
                binding.bottomNavView.gone()
            }
        }
    }

    override fun onLanguageSelected(code: String) {
        LocalHelper.setLocale(this, code)
        recreate()
    }
}