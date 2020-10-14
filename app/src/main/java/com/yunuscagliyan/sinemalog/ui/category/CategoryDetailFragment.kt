package com.yunuscagliyan.sinemalog.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.yunuscagliyan.sinemalog.MainActivity
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.databinding.FragmentCategoryDetailBinding
import com.yunuscagliyan.sinemalog.ui.adapters.HomeLoadStateAdapter
import com.yunuscagliyan.sinemalog.ui.adapters.MovieDetailAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class CategoryDetailFragment : Fragment(R.layout.fragment_category_detail) {


    private var _binding: FragmentCategoryDetailBinding?=null
    private val binding get()= _binding!!
    private val viewModel by viewModels<CategoryViewModel>()
    private var adapter: MovieDetailAdapter=MovieDetailAdapter()
    val args:CategoryDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getGenreMovies(args.genre.id!!)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentCategoryDetailBinding.bind(view)
        initUI()
        initGenreMoviesObserve()
    }

    private fun initUI() {
        (activity as MainActivity).setUpToolbar(binding.toolbar)
        binding.toolbar.apply {
            title=args.genre.name
        }
        initGenreMovie()
    }

    private fun initGenreMoviesObserve() {
        viewModel.genreMovies.observe(viewLifecycleOwner,{
            this.adapter.submitData(viewLifecycleOwner.lifecycle,it)
        })
    }

    private fun initGenreMovie() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        postponeEnterTransition(250, TimeUnit.MILLISECONDS)
        binding.apply {
            rvGenreMovie.setHasFixedSize(true)
            rvGenreMovie.doOnPreDraw {
                startPostponedEnterTransition()
            }
            rvGenreMovie.layoutManager = layoutManager
            rvGenreMovie.adapter = adapter.withLoadStateHeaderAndFooter(
                header = HomeLoadStateAdapter{adapter.retry()},
                footer = HomeLoadStateAdapter{adapter.retry()}
            )

            buttonRetry.setOnClickListener {
                adapter.retry()
            }
            adapter.addLoadStateListener {loadState->
                binding.apply {
                    rvGenreMovie.isVisible=loadState.refresh is LoadState.NotLoading
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