package com.yunuscagliyan.sinemalog.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.yunuscagliyan.sinemalog.MainActivity
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.databinding.FragmentSettingBinding
import com.yunuscagliyan.sinemalog.utils.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment(R.layout.fragment_setting) {
    private var _binding: FragmentSettingBinding?=null
    private val binding get() = _binding!!
    @Inject
    lateinit var mPref: SharedPref
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentSettingBinding.bind(view)
        initUI()
        initSwitchSwap()
        initAd()
    }


    private fun initAd() {
        val adRequest= AdRequest.Builder()
            .build()
        adRequest.isTestDevice(binding.root.context)
        binding.elementAd.adView.loadAd(adRequest)
    }

    private fun initUI() {
        (activity as MainActivity).setUpToolbar(binding.toolbar)
    }
    private fun initSwitchSwap() {
        binding.switchCompat.isChecked = mPref.nightMode
        binding.switchCompat.setOnCheckedChangeListener { buttonView, isChecked ->
            mPref.nightMode = isChecked
            (activity as MainActivity).recreate()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}