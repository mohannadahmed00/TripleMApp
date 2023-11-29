package com.giraffe.triplemapplication

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.giraffe.triplemapplication.databinding.ActivityMainBinding
import com.giraffe.triplemapplication.utils.LocalHelper
import com.giraffe.triplemapplication.utils.ViewModelFactory
import com.giraffe.triplemapplication.utils.gone
import com.giraffe.triplemapplication.utils.show


class MainActivity : AppCompatActivity(),OnActivityCallback{
    private var context: Context? = null
    companion object{
        private const val TAG = "MainActivity"
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var sharedVM: SharedVM
    private lateinit var factory: ViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*factory = ViewModelFactory(Repo.getInstance(ApiClient, ConcreteLocalSource(this)))
        sharedVM = ViewModelProvider(this, factory)[SharedVM::class.java]
        sharedVM.getLanguage()
        observeGetLanguage()*/
        context = LocalHelper.setLocale(this,LocalHelper.getLanguage(this))

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
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

    /*private fun observeGetLanguage() {
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
        *//*val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(config,baseContext.resources.displayMetrics)*//*

        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(Locale(languageCode))
        conf.setLayoutDirection(Locale(languageCode))
        res.updateConfiguration(conf, dm)
    }*/

    override fun onLanguageSelected(code: String) {
        //setLocale(code)
        //recreate()
        context = LocalHelper.setLocale(this,code)
        recreate()
    }
}