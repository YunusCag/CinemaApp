package com.yunuscagliyan.sinemalog.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.databinding.FragmentOnBoardingBinding
import com.yunuscagliyan.sinemalog.utils.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingFragment : Fragment(R.layout.fragment_on_boarding) {

    private var _binding:FragmentOnBoardingBinding?=null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    @Inject lateinit var mPref: SharedPref
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentOnBoardingBinding.bind(view)
        navController= Navigation.findNavController(view)
        initUI()
    }

    private fun initUI() {
        val textList=resources.getStringArray(R.array.on_boarding_texts)
        val adapter=OnBoardAdapter(textList.toList())
        binding.apply {
            viewPager.adapter=adapter
            dotsIndicator.setViewPager2(viewPager)
        }
        binding.button.setOnClickListener {
            mPref.isFirstTime=false
            navController.navigate(R.id.action_home)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


}