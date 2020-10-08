package com.yunuscagliyan.sinemalog.ui.top_rated

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.yunuscagliyan.sinemalog.MainActivity
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.databinding.FragmentTopRatedBinding
import com.yunuscagliyan.sinemalog.databinding.FragmentUpComingBinding
import com.yunuscagliyan.sinemalog.ui.adapters.HomeLoadStateAdapter
import com.yunuscagliyan.sinemalog.ui.adapters.MovieDetailAdapter
import com.yunuscagliyan.sinemalog.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class TopRatedFragment : Fragment(R.layout.fragment_top_rated) {

    private var _binding: FragmentTopRatedBinding?=null
    private val binding get()=_binding!!
    private val viewModel: HomeViewModel by viewModels<HomeViewModel>()
    private lateinit var adapter: MovieDetailAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentTopRatedBinding.bind(view)
        initUI()
        initUpComingObserve()
    }

    private fun initUpComingObserve() {
        viewModel.topRatedMovies.observe(viewLifecycleOwner,{
            this.adapter.submitData(viewLifecycleOwner.lifecycle,it)
        })
    }

    private fun initUI() {
        (activity as MainActivity).setUpToolbar(binding.toolbar)
        this.adapter= MovieDetailAdapter()
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        postponeEnterTransition(250, TimeUnit.MILLISECONDS)
        binding.apply {
            rvTopRatedMovie.setHasFixedSize(true)
            rvTopRatedMovie.doOnPreDraw {
                startPostponedEnterTransition()
            }
            rvTopRatedMovie.layoutManager = layoutManager
            rvTopRatedMovie.adapter = adapter.withLoadStateHeaderAndFooter(
                header = HomeLoadStateAdapter{adapter.retry()},
                footer = HomeLoadStateAdapter{adapter.retry()}
            )
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}