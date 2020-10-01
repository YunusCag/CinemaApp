package com.yunuscagliyan.sinemalog.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.databinding.FragmentOnBoardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment : Fragment(R.layout.fragment_on_boarding) {

    private var _binding:FragmentOnBoardingBinding?=null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentOnBoardingBinding.bind(view)
        navController= Navigation.findNavController(view)
        initUI()
    }

    private fun initUI() {
        binding.button.setOnClickListener {
            navController.navigate(R.id.home_next)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


}