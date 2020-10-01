package com.yunuscagliyan.sinemalog.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.databinding.FragmentHomeScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var upComingAdapter: UpComingAdapter
    private lateinit var movieAdapter: MovieAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeScreenBinding.bind(view)
        initUI()
        initUpComingObserve()
        initPopularObserve()
    }

    private fun initUI() {
        initUpComingMovies()
        initPopularMovie()
    }

    private fun initUpComingObserve() {
        viewModel.upComingMovies.observe(viewLifecycleOwner) {
            upComingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }
    private fun initPopularObserve(){
        viewModel.popularMovies.observe(viewLifecycleOwner,{
            movieAdapter.submitData(viewLifecycleOwner.lifecycle,it)
        })
    }
    private fun initPopularMovie(){
        this.movieAdapter= MovieAdapter()
        val layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.apply {
            rvPopularMovie.setHasFixedSize(true)
            rvPopularMovie.layoutManager=layoutManager
            rvPopularMovie.adapter=movieAdapter
        }

    }


    private fun initUpComingMovies() {
        upComingAdapter = UpComingAdapter()
        binding.apply {
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(rvUpComingMovie)
            rvUpComingMovie.setHasFixedSize(true)
            rvUpComingMovie.layoutManager = layoutManager
            rvUpComingMovie.adapter = upComingAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            var position = 0
            while (true) {
                delay(2000L)

                binding.apply {
                    if (rvUpComingMovie.adapter!!.itemCount > position) {
                        rvUpComingMovie.smoothScrollToPosition(position)
                        position++
                    } else {
                        position = 0
                    }

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}