package com.yunuscagliyan.sinemalog.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.yunuscagliyan.sinemalog.MainActivity
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.databinding.FragmentHomeScreenBinding
import com.yunuscagliyan.sinemalog.ui.adapters.HomeLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var upComingAdapter: UpComingAdapter
    private lateinit var popularAdapter: MovieAdapter
    private lateinit var trendingAdapter: MovieAdapter
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeScreenBinding.bind(view)
        navController=Navigation.findNavController(view)
        initUI()
        initUpComingObserve()
        initPopularObserve()
        initTrendingObserve()

    }

    private fun initUI() {
        (activity as MainActivity).setUpToolbar(binding.toolbar)
        initUpComingMovies()
        initPopularMovie()
        initTrendingMovies()
        binding.apply {
            tvPopular.setOnClickListener {
                navController.navigate(HomeScreenFragmentDirections.popularViewAll())
            }
            tvTrending.setOnClickListener {
                navController.navigate(HomeScreenFragmentDirections.trendingViewAll())
            }
            tvUpcoming.setOnClickListener {
                navController.navigate(HomeScreenFragmentDirections.upComingViewAll())
            }
        }
    }
    private fun initUpComingObserve() {
        viewModel.upComingMovies.observe(viewLifecycleOwner) {
            upComingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    private fun initPopularObserve() {
        viewModel.popularMovies.observe(viewLifecycleOwner, {
            popularAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        })
    }

    private fun initTrendingObserve() {
        viewModel.trendingMovies.observe(viewLifecycleOwner, {
            trendingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        })
    }

    private fun initPopularMovie() {
        this.popularAdapter = MovieAdapter()
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        postponeEnterTransition(250, TimeUnit.MILLISECONDS)
        binding.apply {
            rvPopularMovie.setHasFixedSize(true)
            rvPopularMovie.layoutManager = layoutManager
            rvPopularMovie.doOnPreDraw {
                startPostponedEnterTransition()
            }
            rvPopularMovie.adapter = popularAdapter.withLoadStateHeaderAndFooter(
                header = HomeLoadStateAdapter{popularAdapter.retry()},
                footer = HomeLoadStateAdapter{popularAdapter.retry()}
            )
        }

    }


    private fun initUpComingMovies() {
        upComingAdapter = UpComingAdapter()
        binding.apply {
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            postponeEnterTransition(250,TimeUnit.MILLISECONDS)
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(rvUpComingMovie)
            rvUpComingMovie.setHasFixedSize(true)
            rvUpComingMovie.doOnPreDraw {
                startPostponedEnterTransition()
            }
            rvUpComingMovie.layoutManager = layoutManager
            rvUpComingMovie.adapter = upComingAdapter.withLoadStateHeaderAndFooter(
                header = HomeLoadStateAdapter{upComingAdapter.retry()},
                footer = HomeLoadStateAdapter{upComingAdapter.retry()}
            )
        }
        /*
        viewLifecycleOwner.lifecycleScope.launch {
            var position = 0
            var isIncrement: Boolean = true
            while (true) {
                delay(2000L)

                binding.apply {
                    if (10 > position && isIncrement) {
                        ++position
                    }
                    if (position == 10) {
                        isIncrement = false
                    }
                    if (!isIncrement) {
                        position -= 1
                    }
                    if (position == 0) {
                        isIncrement = true
                    }
                    rvUpComingMovie.smoothScrollToPosition(position)


                }
            }
        }

         */
    }

    private fun initTrendingMovies() {
        this.trendingAdapter = MovieAdapter()
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        postponeEnterTransition(250,TimeUnit.MILLISECONDS)
        binding.apply {
            rvTrendingMovie.setHasFixedSize(true)
            rvTrendingMovie.doOnPreDraw {
                startPostponedEnterTransition()
            }
            rvTrendingMovie.layoutManager = layoutManager
            rvTrendingMovie.adapter = trendingAdapter.withLoadStateHeaderAndFooter(
                header = HomeLoadStateAdapter{trendingAdapter.retry()},
                footer = HomeLoadStateAdapter{trendingAdapter.retry()}
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}