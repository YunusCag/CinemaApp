package com.yunuscagliyan.sinemalog

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.ads.MobileAds
import com.shreyaspatil.MaterialDialog.AbstractDialog
import com.shreyaspatil.MaterialDialog.MaterialDialog
import com.yunuscagliyan.sinemalog.databinding.ActivityMainBinding
import com.yunuscagliyan.sinemalog.utils.AppConstant
import com.yunuscagliyan.sinemalog.utils.SharedPref
import com.yunuscagliyan.sinemalog.utils.ThemeHelper
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    val NOTIFICATION_BROADCAST=100
    @Inject
    lateinit var mPref: SharedPref

    private var destinationChangeCount = AppConstant.SHOW_APP_RATE_DIALOG_CONTROL
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTheme()
        initUI()
        initAdmob()
        setUpDestinationChangeListener()
        setUpSideNavMenu()
        initWorkManager()

    }

    private fun initWorkManager() {

        val alarmManager=getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent=Intent("android.media.action.DISPLAY_NOTIFICATION")
        notificationIntent.addCategory("android.intent.category.DEFAULT")
        val broadcast=PendingIntent
            .getBroadcast(this,NOTIFICATION_BROADCAST,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar= Calendar.getInstance()
        calendar.add(Calendar.MINUTE,15)
        alarmManager.setRepeating(AlarmManager.RTC,calendar.timeInMillis,
        AlarmManager.INTERVAL_DAY,broadcast)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,broadcast)
    }

    private fun initAdmob() {
        MobileAds.initialize(this)
    }

    private fun initTheme() {
        if (mPref.nightMode) {
            ThemeHelper.applyTheme(ThemeHelper.DARK_MODE)
        } else {
            ThemeHelper.applyTheme(ThemeHelper.LIGHT_MODE)
        }
    }

    private fun initUI() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setUpDestinationChangeListener() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (!mPref.isAppRated) {
                decreaseDestinationCount()
            }
            if (destination.id == R.id.destination_home) {
                showNoInternetConnection()
                binding.navigationLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                binding.navigationLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
            if (isAppRateShow()) {
                showRateDialog()
            }

        }
    }

    private fun showNoInternetConnection() {
        if (!hasInternetConnection()) {
            val appName = resources.getString(R.string.app_name)
            val internetMessage = resources.getString(R.string.no_internet_connection)
            val positiveText = resources.getString(R.string.rate_dialog_possitive)
            val mDialog = MaterialDialog.Builder(this)
                .setTitle(appName)
                .setMessage(internetMessage)
                .setAnimation(R.raw.lottie_internet_connection)
                .setPositiveButton(
                    positiveText,
                    AbstractDialog.OnClickListener { dialogInterface, which ->
                        dialogInterface.dismiss()
                    })
                .build()
            mDialog.show()
        }
    }

    private fun showRateDialog() {
        val rateAppMessage = resources.getString(R.string.app_rated_dialog)
        val appName = resources.getString(R.string.app_name)
        val negativeText = resources.getString(R.string.rate_dialog_negative)
        val positiveText = resources.getString(R.string.rate_dialog_possitive)
        val findMarketAppMessage = resources.getString(R.string.unable_find_market_app)
        val mDialog = MaterialDialog.Builder(this)
            .setAnimation(R.raw.lottie_rate_animation)
            .setMessage(rateAppMessage)
            .setTitle(appName)
            .setNegativeButton(
                negativeText,
                AbstractDialog.OnClickListener { dialogInterface, which ->
                    dialogInterface.dismiss()
                })
            .setPositiveButton(
                positiveText,
                AbstractDialog.OnClickListener { dialogInterface, which ->
                    val uri = Uri.parse("market://details?id=$packageName")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    try {
                        dialogInterface.dismiss()
                        mPref.isAppRated = true
                        startActivity(intent)
                    } catch (exception: ActivityNotFoundException) {
                        Toast.makeText(this, findMarketAppMessage, Toast.LENGTH_LONG).show()
                    }
                })
            .build()
        mDialog.show()
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }

            }
        }
        return false
    }

    private fun decreaseDestinationCount() {
        if (destinationChangeCount == 0) {
            destinationChangeCount = 5
        }
        destinationChangeCount -= 1
    }

    private fun isAppRateShow(): Boolean {
        return destinationChangeCount == 0 && !this.mPref.isAppRated
    }

    private fun setUpSideNavMenu() {
        //navigaton Controller SetUp
        binding.navigationDrawer.let {
            NavigationUI.setupWithNavController(it, navController)
        }
        binding.navigationDrawer.itemIconTintList = null
        //Side Design SetUp
        var actionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            binding.navigationLayout, null,
            R.string.app_name, R.string.app_name
        ) {

            private var scaleFactor: Float = 6f
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                super.onDrawerSlide(drawerView, slideOffset)
                val slideX: Float = drawerView.width * slideOffset
                binding.contentLayout.translationX = slideX
                binding.contentLayout.scaleX = (1 - (slideOffset / scaleFactor))
                binding.contentLayout.scaleY = (1 - (slideOffset / scaleFactor))
            }

        }
        binding.navigationLayout.setScrimColor(Color.TRANSPARENT)
        binding.navigationLayout.drawerElevation = 0f
        binding.navigationLayout.addDrawerListener(actionBarDrawerToggle)
    }

    fun setUpToolbar(toolbar: Toolbar) {
        val appBarConfiguration = AppBarConfiguration.Builder(
            setOf<Int>(
                R.id.destination_home,
            )
        ).setDrawerLayout(binding.navigationLayout).build()
        setSupportActionBar(toolbar)
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)
    }
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            navController,
            binding.navigationLayout
        ) || super.onSupportNavigateUp()
    }
}