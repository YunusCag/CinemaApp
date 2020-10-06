package com.yunuscagliyan.sinemalog.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.yunuscagliyan.sinemalog.MainActivity
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.databinding.FragmentCategoryBinding
import com.yunuscagliyan.sinemalog.eventbus.GenreEvent
import com.yunuscagliyan.sinemalog.utils.AppConstant
import com.yunuscagliyan.sinemalog.utils.DataState
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class CategoryFragment : Fragment(R.layout.fragment_category) {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoryViewModel by viewModels()
    private val adapter = CategoryAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCategoryBinding.bind(view)
        initObserve()
        initUI()
    }

    private fun initUI() {
        (activity as MainActivity).setUpToolbar(binding.toolbar)
        viewModel.setStateEvent(CategoryEventState.GetGenreList)
        binding.apply {
            val layoutManager =
                LinearLayoutManager(
                    context, LinearLayoutManager.VERTICAL,false)
            rvGenreList.setHasFixedSize(true)
            rvGenreList.layoutManager=layoutManager
            rvGenreList.adapter=adapter

        }

    }

    private fun initObserve() {
        viewModel.genreListDataState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is DataState.Success -> {
                    binding.apply {
                        layoutProgress.visibility = View.GONE
                    }
                    adapter.submitList(state.data)
                }
                is DataState.Loading -> {
                    displayProgress()
                }
                is DataState.Error -> {
                    displayError()
                }
            }
        })
    }

    private fun displayProgress() {
        binding.apply {
            layoutProgress.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
            buttonRetry.visibility = View.GONE
            textViewError.visibility = View.GONE
        }
    }

    private fun displayError() {
        binding.apply {
            layoutProgress.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            textViewError.visibility = View.VISIBLE
            buttonRetry.visibility = View.VISIBLE
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getGenreMovieList(genre:GenreEvent){
        viewModel.getGenreMovies(genre.genreId)
    }
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}