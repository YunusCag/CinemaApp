package com.yunuscagliyan.sinemalog.ui.movie_detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log

import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.yunuscagliyan.sinemalog.MainActivity
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.data.models.MovieDetail
import com.yunuscagliyan.sinemalog.databinding.FragmentMovieDetailBinding
import com.yunuscagliyan.sinemalog.ui.adapters.HomeLoadStateAdapter
import com.yunuscagliyan.sinemalog.ui.home.MovieAdapter
import com.yunuscagliyan.sinemalog.utils.AppConstant
import com.yunuscagliyan.sinemalog.utils.DataState
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieDetailViewModel by viewModels()
    val args: MovieDetailFragmentArgs by navArgs()
    private lateinit var navController: NavController
    private val castAdapter = CastAdapter()
    private val movieAdapter = MovieAdapter()
    private lateinit var mInterstitial: InterstitialAd


    private fun setUpShareAnimation() {
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieDetailBinding.bind(view)
        this.navController=Navigation.findNavController(view)
        initUI()
        setUpShareAnimation()
        postponeEnterTransition(250, TimeUnit.MILLISECONDS)

        gettingMovieDetail()
        initMovieDetailObserve()
        initSimilarMovieObserve()

        initCastList()
        initSimilarMovieList()

        initAd()

    }
    private fun initSimilarMovieList() {
        binding.apply {
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvSimilarMovie.setHasFixedSize(true)
            rvSimilarMovie.layoutManager = layoutManager
            rvSimilarMovie.doOnPreDraw {
                startPostponedEnterTransition()
            }
            rvSimilarMovie.adapter = movieAdapter.withLoadStateHeaderAndFooter(
                header = HomeLoadStateAdapter { movieAdapter.retry() },
                footer = HomeLoadStateAdapter { movieAdapter.retry() }
            )
        }
    }
    private fun initAd() {
        val adRequest=AdRequest.Builder()
            .build()
        binding.elementAd.adView.loadAd(adRequest)

        mInterstitial= InterstitialAd(context)
        mInterstitial.adUnitId=AppConstant.INTERSTITIAL_AD_ID
        mInterstitial.loadAd(adRequest)
    }


    private fun initSimilarMovieObserve() {
        viewModel.getSimilarMovie(args.movieId).observe(viewLifecycleOwner, {
            this.movieAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        })
    }

    private fun initCastList() {
        binding.apply {
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvMovieCast.setHasFixedSize(true)
            rvMovieCast.doOnPreDraw {
                startPostponedEnterTransition()
            }
            rvMovieCast.layoutManager = layoutManager
            rvMovieCast.adapter = castAdapter
        }
    }


    private fun initMovieDetailObserve() {
        viewModel.detailDataState.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    binding.apply {
                        layoutBottom.visibility=View.VISIBLE
                    }
                    bindUI(dataState.data)
                }
                is DataState.Error -> {
                    binding.apply {
                        layoutBottom.visibility=View.GONE
                    }
                    displayError()
                }
                is DataState.Loading -> {
                    binding.apply {
                        layoutBottom.visibility=View.GONE
                    }
                    displayProgress()
                }
            }
        })
        viewModel.castDataState.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    this.castAdapter.submitList(dataState.data.cast!!)
                }
            }

        })
    }

    private fun displayProgress() {
        binding.apply {
            layoutLoading.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
            buttonRetry.visibility = View.GONE
            textViewError.visibility = View.GONE
        }
    }

    private fun displayError() {
        binding.apply {
            layoutLoading.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            textViewError.visibility = View.VISIBLE
            buttonRetry.visibility = View.VISIBLE
            ivMoviePoster.setImageResource(R.drawable.cinema_icon)
        }
    }

    private fun bindUI(detail: MovieDetail) {
        binding.apply {
            layoutLoading.visibility = View.GONE
            toolbar.title = detail.title
        }
        binding.apply {
            toolbarLayout.title = detail.title
            tvMovieOverview.text = detail.overview
            tvMovieTime.text = "${detail.runtime}"

            val convertFormatter = SimpleDateFormat("yyyy-MM-dd")
            var releaseDate = convertFormatter.parse(detail.releaseDate)
            val date = DateFormat.getDateInstance().format(releaseDate)
            tvMovieReleaseDate.text = date
        }

    }

    private fun bindImage(moviePosterURL: String) {
        binding.ivMoviePoster.apply {
            transitionName = moviePosterURL
            Glide.with(this)
                .load(moviePosterURL)
                .dontAnimate()
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }

                })
                .error(R.drawable.ic_error)
                .into(this)
        }

    }

    private fun initUI() {
        (activity as MainActivity).setUpToolbar(binding.toolbar)
        binding.btnNavigateTrailer.setOnClickListener {
            if(mInterstitial.isLoaded){
                mInterstitial.show()
            }
            val bundle= bundleOf("movieId" to args.movieId)
            this.navController.navigate(R.id.action_trailer,bundle)
        }
        binding.apply {
            buttonRetry.setOnClickListener {
                gettingMovieDetail()
                if(mInterstitial.isLoaded){
                    mInterstitial.show()
                }
                initAd()
                initSimilarMovieObserve()
            }
        }
    }

    private fun gettingMovieDetail() {
        if (args.movieId != -1 && args.moviePosterURL != null) {
            viewModel.setStateEvent(MovieDetailStateEvent.GetSimilarMovie(args.movieId))
            viewModel.setStateEvent(MovieDetailStateEvent.GetMovieDetail(args.movieId))
            viewModel.setStateEvent(MovieDetailStateEvent.GetCasts(args.movieId))
            binding.apply {
                toolbarLayout.transitionName = "${args.movieId}"
            }
            bindImage(args.moviePosterURL!!)

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


}