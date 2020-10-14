package com.yunuscagliyan.sinemalog.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitial
import com.yunuscagliyan.sinemalog.MainActivity
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.databinding.FragmentHomeScreenBinding
import com.yunuscagliyan.sinemalog.ui.adapters.HomeLoadStateAdapter
import com.yunuscagliyan.sinemalog.ui.adapters.MovieDetailAdapter
import com.yunuscagliyan.sinemalog.utils.AppConstant
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
    private lateinit var topRatedAdapter:MovieAdapter
    private lateinit var navController: NavController
    private lateinit var mInterstitial: InterstitialAd

    private var clickCount=AppConstant.CLICK_CONTROL_INITIAL
    private var isAdShow=false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeScreenBinding.bind(view)
        navController=Navigation.findNavController(view)
        initUI()
        initAd()
        initUpComingObserve()
        initPopularObserve()
        initTrendingObserve()
        initTopRatedObserve()

    }

    private fun initAd() {
        mInterstitial= InterstitialAd(context)
        mInterstitial.adUnitId=AppConstant.INTERSTITIAL_AD_ID
        mInterstitial.loadAd(AdRequest.Builder().build())
    }

    private fun isInterstitialAdShow():Boolean{
        return mInterstitial.isLoaded&&isAdShow
    }

    private fun decreaseControl(){
        this.clickCount-=1
        Log.e("TAG","Click count:$clickCount")
        if(this.clickCount==0){
            this.isAdShow=true
            this.clickCount=AppConstant.CLICK_CONTROL_INITIAL
        }else{
            this.isAdShow=false
        }
    }

    private fun initUI() {
        (activity as MainActivity).setUpToolbar(binding.toolbar)
        initUpComingMovies()
        initPopularMovie()
        initTrendingMovies()
        initTopRatedMovies()
        binding.apply {
            tvPopular.setOnClickListener {
                showInterstitialAd()
                navController.navigate(HomeScreenFragmentDirections.popularViewAll())
                decreaseControl()
            }
            tvTrending.setOnClickListener {
                showInterstitialAd()
                navController.navigate(HomeScreenFragmentDirections.trendingViewAll())
                decreaseControl()
            }
            tvUpcoming.setOnClickListener {
                showInterstitialAd()
                navController.navigate(HomeScreenFragmentDirections.upComingViewAll())
                decreaseControl()
            }
            tvTopRated.setOnClickListener {
                showInterstitialAd()
                navController.navigate(HomeScreenFragmentDirections.topRatedViewAll())
                decreaseControl()
            }
        }
    }

    private fun showInterstitialAd() {
        if (isInterstitialAdShow()) {
            mInterstitial.show()
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
    private fun initTopRatedObserve() {
        viewModel.topRatedMovies.observe(viewLifecycleOwner,{
            topRatedAdapter.submitData(viewLifecycleOwner.lifecycle,it)
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
            buttonRetry.setOnClickListener {
                popularAdapter.retry()
                upComingAdapter.retry()
                topRatedAdapter.retry()
                trendingAdapter.retry()

            }
            popularAdapter.addLoadStateListener {loadState->
                binding.apply {
                    rvPopularMovie.isVisible=loadState.refresh is LoadState.NotLoading
                    progressBar.isVisible=loadState.refresh is LoadState.Loading
                    buttonRetry.isVisible=loadState.refresh is LoadState.Error
                    textViewError.isVisible=loadState.refresh is LoadState.Error
                }

            }
        }

    }
    private fun initTopRatedMovies() {
        this.topRatedAdapter = MovieAdapter()
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        postponeEnterTransition(250, TimeUnit.MILLISECONDS)
        binding.apply {
            rvTopRatedMovie.setHasFixedSize(true)
            rvTopRatedMovie.layoutManager = layoutManager
            rvTopRatedMovie.doOnPreDraw {
                startPostponedEnterTransition()
            }
            rvTopRatedMovie.adapter = topRatedAdapter.withLoadStateHeaderAndFooter(
                header = HomeLoadStateAdapter{topRatedAdapter.retry()},
                footer = HomeLoadStateAdapter{topRatedAdapter.retry()}
            )
            topRatedAdapter.addLoadStateListener {loadState->
                binding.apply {
                    rvTopRatedMovie.isVisible=loadState.refresh is LoadState.NotLoading
                    progressBar.isVisible=loadState.refresh is LoadState.Loading
                    buttonRetry.isVisible=loadState.refresh is LoadState.Error
                    textViewError.isVisible=loadState.refresh is LoadState.Error
                }

            }
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
            upComingAdapter.addLoadStateListener {loadState->
                binding.apply {
                    rvUpComingMovie.isVisible=loadState.refresh is LoadState.NotLoading
                    progressBar.isVisible=loadState.refresh is LoadState.Loading
                    buttonRetry.isVisible=loadState.refresh is LoadState.Error
                    textViewError.isVisible=loadState.refresh is LoadState.Error
                }

            }
        }

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
            trendingAdapter.addLoadStateListener {loadState->
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
        _binding = null
    }


}