package com.yunuscagliyan.sinemalog.ui.credit

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.yunuscagliyan.sinemalog.MainActivity
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.data.api.POSTER_BASE_URL
import com.yunuscagliyan.sinemalog.data.models.CreditResponse
import com.yunuscagliyan.sinemalog.databinding.FragmentCreditBinding
import com.yunuscagliyan.sinemalog.ui.movie_detail.MovieDetailStateEvent
import com.yunuscagliyan.sinemalog.ui.movie_detail.MovieDetailViewModel
import com.yunuscagliyan.sinemalog.utils.DataState
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class CreditFragment : Fragment(R.layout.fragment_credit) {

    private var _binding: FragmentCreditBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieDetailViewModel by viewModels()
    private lateinit var creditResponse: CreditResponse
    private lateinit var adapter: CreditAdapter
    private val args by navArgs<CreditFragmentArgs>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreditBinding.bind(view)
        initUI()
        setUpShareAnimation()
        postponeEnterTransition(250, TimeUnit.MILLISECONDS)
        initCreditObserve()
        initMovieList()
    }


    private fun setUpShareAnimation() {
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    private fun initMovieList() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        postponeEnterTransition(250, TimeUnit.MILLISECONDS)
        binding.apply {
            rvCreditMovie.setHasFixedSize(true)
            rvCreditMovie.layoutManager = layoutManager
            rvCreditMovie.doOnPreDraw {
                startPostponedEnterTransition()
            }
            rvCreditMovie.adapter = adapter

        }
    }

    private fun initCreditObserve() {
        viewModel.creditDataState.observe(viewLifecycleOwner, { state ->
            when (state) {
                is DataState.Success -> {
                    creditResponse = state.data
                    if (creditResponse.person!!.knownFor != null && creditResponse.person!!.knownFor!!.isNotEmpty()) {
                        this.adapter.submitList(state.data.person!!.knownFor!!)
                    }
                    binding.toolbar.title = state.data.person!!.name
                    binding.toolbarLayout.title=state.data.person!!.name
                }
            }
        })
    }

    private fun bindImage(profilePath: String) {
        binding.ivCreditProfile.apply {
            transitionName = profilePath
            Glide.with(this)
                .load(profilePath)
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
        this.adapter = CreditAdapter()
        (activity as MainActivity).setUpToolbar(binding.toolbar)
        bindImage(args.profileURL)
        viewModel.setStateEvent(MovieDetailStateEvent.GetCredit(args.creditId))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}