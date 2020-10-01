package com.yunuscagliyan.sinemalog.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.databinding.FragmentOnBoardingBinding
import com.yunuscagliyan.sinemalog.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private var _binding: FragmentSplashBinding?=null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentSplashBinding.bind(view)
        navController=Navigation.findNavController(view)
        initUI()
    }

    private fun initUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(1000L)
            navController.navigate(R.id.onboarding_next)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


}