package com.yunuscagliyan.sinemalog.ui.popular

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.yunuscagliyan.sinemalog.MainActivity
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.databinding.FragmentPopularMovieBinding
import com.yunuscagliyan.sinemalog.ui.adapters.HomeLoadStateAdapter
import com.yunuscagliyan.sinemalog.ui.adapters.MovieDetailAdapter
import com.yunuscagliyan.sinemalog.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class PopularMovieFragment : Fragment(R.layout.fragment_popular_movie) {


    private var _binding:FragmentPopularMovieBinding?=null
    private val binding get()= _binding!!
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var adapter:MovieDetailAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding=FragmentPopularMovieBinding.bind(view)
        initUI()
        initPopularObserve()
    }

    private fun initPopularObserve() {
        viewModel.popularMovies.observe(viewLifecycleOwner,{
            adapter.submitData(viewLifecycleOwner.lifecycle,it)
        })
    }

    private fun initUI() {
        (activity as MainActivity).setUpToolbar(binding.toolbar)
        initPopularMovie()
    }

    private fun initPopularMovie() {
        this.adapter = MovieDetailAdapter()
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        postponeEnterTransition(250,TimeUnit.MILLISECONDS)
        binding.apply {
            rvPopularMovie.setHasFixedSize(true)
            rvPopularMovie.doOnPreDraw {
                startPostponedEnterTransition()
            }
            rvPopularMovie.layoutManager = layoutManager
            rvPopularMovie.adapter = adapter.withLoadStateHeaderAndFooter(
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