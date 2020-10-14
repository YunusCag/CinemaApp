package com.yunuscagliyan.sinemalog.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.databinding.FragmentSplashBinding
import com.yunuscagliyan.sinemalog.utils.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private var _binding: FragmentSplashBinding?=null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    @Inject lateinit var mPref: SharedPref
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentSplashBinding.bind(view)
        navController=Navigation.findNavController(view)
        initUI()
    }

    private fun initUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(4000L)
            binding.lottiAnimationView.cancelAnimation()
            if(mPref.isFirstTime){
                navController.navigate(R.id.action_on_board,)
            }else{
                navController.navigate(R.id.action_home)
            }


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


}