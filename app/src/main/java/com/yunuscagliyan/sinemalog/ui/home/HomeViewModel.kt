package com.yunuscagliyan.sinemalog.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yunuscagliyan.sinemalog.data.models.Movie
import com.yunuscagliyan.sinemalog.data.repository.MovieRepository

class HomeViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository
):ViewModel() {
    val upComingMovies:LiveData<PagingData<Movie>> by lazy{
        movieRepository.getUpComingMovie().cachedIn(viewModelScope)
    }
    val popularMovies:LiveData<PagingData<Movie>> by lazy {
        movieRepository.getPopularMovie().cachedIn(viewModelScope)
    }
    val trendingMovies:LiveData<PagingData<Movie>> by lazy {
        movieRepository.getTrendingMovie().cachedIn(viewModelScope)
    }
}