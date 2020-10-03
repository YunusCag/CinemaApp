package com.yunuscagliyan.sinemalog.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.yunuscagliyan.sinemalog.MainActivity
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.databinding.FragmentOnBoardingBinding
import com.yunuscagliyan.sinemalog.databinding.FragmentSettingBinding
import com.yunuscagliyan.sinemalog.utils.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.system.exitProcess

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
    }

    private fun initUI() {
        (activity as MainActivity).setUpToolbar(binding.toolbar)
    }
    private fun initSwitchSwap() {
        binding.switchCompat.isChecked = mPref.nightMode
        binding.switchCompat.setOnCheckedChangeListener { buttonView, isChecked ->
            mPref.nightMode = isChecked
            (activity as MainActivity).recreate()
            /*
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            exitProcess(0);
             */
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}