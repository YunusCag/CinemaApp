package com.yunuscagliyan.sinemalog.ui.trending

import android.os.Bundle
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yunuscagliyan.sinemalog.MainActivity
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.databinding.FragmentTrendingMovieBinding
import com.yunuscagliyan.sinemalog.ui.adapters.HomeLoadStateAdapter
import com.yunuscagliyan.sinemalog.ui.adapters.MovieDetailAdapter
import com.yunuscagliyan.sinemalog.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class TrendingMovieFragment : Fragment(R.layout.fragment_trending_movie) {

    private var _binding: FragmentTrendingMovieBinding?=null
    private val binding get()= _binding!!
    private val viewModel by viewModels<HomeViewModel>()
    private var adapter: MovieDetailAdapter=MovieDetailAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentTrendingMovieBinding.bind(view)
        initUI()
        initPopularObserve()
    }

    private fun initPopularObserve() {
        viewModel.trendingMovies.observe(viewLifecycleOwner,{
            adapter.submitData(viewLifecycleOwner.lifecycle,it)
        })
    }

    private fun initUI() {
        (activity as MainActivity).setUpToolbar(binding.toolbar)
        initPopularMovie()
    }

    private fun initPopularMovie() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        postponeEnterTransition(250, TimeUnit.MILLISECONDS)
        binding.apply {
            rvTrendingMovie.setHasFixedSize(true)
            rvTrendingMovie.doOnPreDraw {
                startPostponedEnterTransition()
            }
            rvTrendingMovie.layoutManager = layoutManager
            rvTrendingMovie.adapter = adapter.withLoadStateHeaderAndFooter(
                header = HomeLoadStateAdapter{adapter.retry()},
                footer = HomeLoadStateAdapter{adapter.retry()}
            )

            buttonRetry.setOnClickListener {
                adapter.retry()
            }
            adapter.addLoadStateListener {loadState->
                binding.apply {
                    rvTrendingMovie.isVisible=loadState.refresh is LoadState.NotLoading
                    progressBar.isVisible=loadState.refresh is LoadState.Loading
                    buttonRetry.isVisible=loadState.refresh is LoadState.Error
                    textViewError.isVisible=loadState.refresh is LoadState.Error
                }

            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


}