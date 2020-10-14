package com.yunuscagliyan.sinemalog.ui.upcoming

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.yunuscagliyan.sinemalog.MainActivity
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.databinding.FragmentUpComingBinding
import com.yunuscagliyan.sinemalog.ui.adapters.HomeLoadStateAdapter
import com.yunuscagliyan.sinemalog.ui.adapters.MovieDetailAdapter
import com.yunuscagliyan.sinemalog.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class UpComingFragment : Fragment(R.layout.fragment_up_coming) {

    private var _binding:FragmentUpComingBinding?=null
    private val binding get()=_binding!!
    private val viewModel:HomeViewModel by viewModels<HomeViewModel>()
    private lateinit var adapter:MovieDetailAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding=FragmentUpComingBinding.bind(view)
        initUI()
        initUpComingObserve()
    }

    private fun initUpComingObserve() {
        viewModel.upComingMovies.observe(viewLifecycleOwner,{
            this.adapter.submitData(viewLifecycleOwner.lifecycle,it)
        })
    }

    private fun initUI() {
        (activity as MainActivity).setUpToolbar(binding.toolbar)
        this.adapter= MovieDetailAdapter()
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        postponeEnterTransition(250, TimeUnit.MILLISECONDS)
        binding.apply {
            rvUpComingMovie.setHasFixedSize(true)
            rvUpComingMovie.doOnPreDraw {
                startPostponedEnterTransition()
            }
            rvUpComingMovie.layoutManager = layoutManager
            rvUpComingMovie.adapter = adapter.withLoadStateHeaderAndFooter(
                header = HomeLoadStateAdapter{adapter.retry()},
                footer = HomeLoadStateAdapter{adapter.retry()}
            )
            buttonRetry.setOnClickListener {
                adapter.retry()
            }
            adapter.addLoadStateListener {loadState->
                binding.apply {
                    rvUpComingMovie.isVisible=loadState.refresh is LoadState.NotLoading
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