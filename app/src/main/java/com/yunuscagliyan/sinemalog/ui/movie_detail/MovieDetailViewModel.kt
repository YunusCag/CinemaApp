package com.yunuscagliyan.sinemalog.ui.movie_detail

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yunuscagliyan.sinemalog.data.models.CastResponse
import com.yunuscagliyan.sinemalog.data.models.Movie
import com.yunuscagliyan.sinemalog.data.models.MovieDetail
import com.yunuscagliyan.sinemalog.data.repository.MovieRepository
import com.yunuscagliyan.sinemalog.utils.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MovieDetailViewModel
@ViewModelInject
constructor(
    private val repository: MovieRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _detailDataState: MutableLiveData<DataState<MovieDetail>> = MutableLiveData()

    val detailDataState: LiveData<DataState<MovieDetail>>
        get() = _detailDataState

    private val _castDataState:MutableLiveData<DataState<CastResponse>> = MutableLiveData()
    val castDataState:MutableLiveData<DataState<CastResponse>> =_castDataState

    private var _similarMovie:LiveData<PagingData<Movie>> =MutableLiveData()
    val similarMovie:LiveData<PagingData<Movie>> =_similarMovie
    fun getSimilarMovie(movieId: Int)=this.repository.getSimilarMovie(movieId)
    fun setStateEvent(state: MovieDetailStateEvent) {
        viewModelScope.launch {
            when (state) {
                is MovieDetailStateEvent.GetMovieDetail -> {
                    repository.getMovieDetail(state.movieId)
                        .onEach {
                            _detailDataState.value = it
                        }
                        .launchIn(viewModelScope)
                }
                is MovieDetailStateEvent.GetCasts->{
                    repository.getMovieCasts(state.movieId)
                        .onEach {
                            _castDataState.value=it
                        }
                        .launchIn(viewModelScope)
                }
                is MovieDetailStateEvent.GetSimilarMovie->{
                    _similarMovie=repository.getSimilarMovie(state.movieId).cachedIn(viewModelScope)
                }
            }
        }
    }
}

sealed class MovieDetailStateEvent {

    class GetMovieDetail(val movieId: Int) : MovieDetailStateEvent()

    class GetCasts(val movieId: Int) : MovieDetailStateEvent()
    class GetSimilarMovie(val movieId:Int):MovieDetailStateEvent()
}