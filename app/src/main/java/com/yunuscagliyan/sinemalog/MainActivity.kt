package com.yunuscagliyan.sinemalog

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.ads.MobileAds
import com.yunuscagliyan.sinemalog.databinding.ActivityMainBinding
import com.yunuscagliyan.sinemalog.utils.SharedPref
import com.yunuscagliyan.sinemalog.utils.ThemeHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding:ActivityMainBinding
    @Inject lateinit var mPref:SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTheme()
        initUI()
        initAdmob()
        setUpDestinationChangeListener()
        setUpSideNavMenu()

    }

    private fun initAdmob() {
        MobileAds.initialize(this)
    }

    private fun initTheme() {
        if(mPref.nightMode){
            ThemeHelper.applyTheme(ThemeHelper.DARK_MODE)
        }else{
            ThemeHelper.applyTheme(ThemeHelper.LIGHT_MODE)
        }
    }

    private fun initUI() {
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController=navHostFragment.navController
    }
    private fun setUpDestinationChangeListener() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if(destination.id==R.id.destination_home){
                binding.navigationLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }else{
                binding.navigationLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }
    }
    private fun setUpSideNavMenu() {
        //navigaton Controller SetUp
        binding.navigationDrawer.let {
            NavigationUI.setupWithNavController(it,navController)
        }
        binding.navigationDrawer.itemIconTintList = null
        //Side Design SetUp
        var actionBarDrawerToggle=object: ActionBarDrawerToggle(this,
            binding.navigationLayout,null,
            R.string.app_name,R.string.app_name){

            private var scaleFactor:Float=6f
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                super.onDrawerSlide(drawerView, slideOffset)
                val slideX:Float=drawerView.width*slideOffset
                binding.contentLayout.translationX=slideX
                binding.contentLayout.scaleX=(1-(slideOffset/scaleFactor))
                binding.contentLayout.scaleY=(1-(slideOffset/scaleFactor))
            }

        }
        binding.navigationLayout.setScrimColor(Color.TRANSPARENT)
        binding.navigationLayout.drawerElevation=0f
        binding.navigationLayout.addDrawerListener(actionBarDrawerToggle)
    }
    fun setUpToolbar(toolbar: Toolbar){
        val appBarConfiguration= AppBarConfiguration.Builder(setOf<Int>(
            R.id.destination_home,
        )).setDrawerLayout(binding.navigationLayout).build()
        setSupportActionBar(toolbar)
        NavigationUI.setupWithNavController(toolbar,navController,appBarConfiguration)
    }
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,binding.navigationLayout)|| super.onSupportNavigateUp()
    }
}