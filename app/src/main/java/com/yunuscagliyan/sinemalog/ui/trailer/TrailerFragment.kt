package com.yunuscagliyan.sinemalog.ui.trailer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.yunuscagliyan.sinemalog.MainActivity
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.databinding.FragmentTrailerBinding
import com.yunuscagliyan.sinemalog.ui.movie_detail.MovieDetailStateEvent
import com.yunuscagliyan.sinemalog.ui.movie_detail.MovieDetailViewModel
import com.yunuscagliyan.sinemalog.utils.DataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrailerFragment : Fragment(R.layout.fragment_trailer) {
    private var _binding:FragmentTrailerBinding?=null
    private val binding get()=_binding!!
    private val viewModel: MovieDetailViewModel by viewModels()
    private lateinit var adapter:TrailerAdapter
    private val args by navArgs<TrailerFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentTrailerBinding.bind(view)
        initUI()
        initTrailerObserve()
        initTrailerList()
    }

    private fun initTrailerList() {
        val layoutManger=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.apply {
            rvTrailer.layoutManager=layoutManger
            rvTrailer.setHasFixedSize(true)
            rvTrailer.adapter=adapter
        }
    }

    private fun initTrailerObserve() {
        viewModel.trailerDataState.observe(viewLifecycleOwner,{state->
            when(state){
                is DataState.Success->{
                    if(state.data.videos!!.size>2){
                        this.adapter.submitList(state.data.videos!!.subList(0,1))
                    }else{
                        this.adapter.submitList(state.data.videos!!)
                    }

                }
            }
        })
    }

    private fun initUI() {
        this.adapter= TrailerAdapter()
        viewModel.setStateEvent(MovieDetailStateEvent.GetMovieTrailer(args.movieId))
        (activity as MainActivity).setUpToolbar(binding.toolbar)

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}