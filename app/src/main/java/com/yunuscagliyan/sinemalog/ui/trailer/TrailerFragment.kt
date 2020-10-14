package com.yunuscagliyan.sinemalog.ui.trailer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
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
    private val args by navArgs<TrailerFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentTrailerBinding.bind(view)
        lifecycle.addObserver(binding.youtubePlayerView)
        initUI()
        initTrailerObserve()
    }



    private fun initTrailerObserve() {
        viewModel.trailerDataState.observe(viewLifecycleOwner,{state->
            when(state){
                is DataState.Success->{
                    if(state.data.videos!!.isNotEmpty()){
                        val video=state.data.videos!![0]
                        binding.apply {
                            val videoId=video!!.key
                            youtubePlayerView.initialize({ youTubePlayer ->
                                youTubePlayer.addListener(object: AbstractYouTubePlayerListener(){
                                    override fun onReady() {
                                        youTubePlayer.loadVideo(videoId!!,0f)
                                        youTubePlayer.pause()
                                    }
                                })
                            },true)
                        }
                    }

                }
            }
        })
    }

    private fun initUI() {
        viewModel.setStateEvent(MovieDetailStateEvent.GetMovieTrailer(args.movieId))
        //(activity as MainActivity).setUpToolbar(binding.toolbar)

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}