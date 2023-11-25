package com.giraffe.triplemapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.giraffe.triplemapplication.database.ConcreteLocalSource
import com.giraffe.triplemapplication.databinding.ActivityMainBinding
import com.giraffe.triplemapplication.model.repo.Repo
import com.giraffe.triplemapplication.network.ApiClient
import com.giraffe.triplemapplication.utils.Constants
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.ViewModelFactory
import com.giraffe.triplemapplication.utils.gone
import com.giraffe.triplemapplication.utils.show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale


class MainActivity : AppCompatActivity(),OnActivityCallback{
    companion object{
        private const val TAG = "MainActivity"
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var sharedVM: SharedVM
    private lateinit var factory: ViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = ViewModelFactory(Repo.getInstance(ApiClient, ConcreteLocalSource(this)))
        sharedVM = ViewModelProvider(this, factory)[SharedVM::class.java]
        sharedVM.getLanguage()
        observeGetLanguage()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(binding.bottomNavView, navController)
        navController.addOnDestinationChangedListener { _: NavController?, navDestination: NavDestination, bundle: Bundle? ->
            if (navDestination.id == R.id.homeFragment || navDestination.id == R.id.searchFragment || navDestination.id == R.id.cartFragment || navDestination.id == R.id.profileFragment) {
                binding.bottomNavView.show()
            } else {
                binding.bottomNavView.gone()
            }
        }
    }

    private fun observeGetLanguage() {
        lifecycleScope.launch (Dispatchers.Main){
            sharedVM.languageFlow.collect{
                when(it){
                    is Resource.Failure -> {
                        setLocale("ar")
                    }
                    Resource.Loading -> {
                        setLocale("ar")
                    }
                    is Resource.Success -> {
                        Log.i(TAG, "observeGetLanguage: ${it.value}")
                        if (it.value== Constants.Languages.ARABIC.value){
                            setLocale("ar")
                        }else{
                            setLocale("en")
                        }
                    }
                }
            }
        }

    }

    private fun setLocale(languageCode: String) {
        /*val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(config,baseContext.resources.displayMetrics)*/

        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(Locale(languageCode))
        conf.setLayoutDirection(Locale(languageCode))
        res.updateConfiguration(conf, dm)
    }

    override fun onLanguageSelected(code: String) {
        setLocale(code)
        recreate()
    }
}