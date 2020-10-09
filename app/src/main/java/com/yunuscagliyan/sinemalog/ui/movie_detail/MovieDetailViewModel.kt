package com.yunuscagliyan.sinemalog.ui.movie_detail

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yunuscagliyan.sinemalog.data.models.*
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

    private val _castDataState: MutableLiveData<DataState<CastResponse>> = MutableLiveData()
    val castDataState: MutableLiveData<DataState<CastResponse>> = _castDataState

    private var _similarMovie: LiveData<PagingData<Movie>> = MutableLiveData()

    val similarMovie: LiveData<PagingData<Movie>> = _similarMovie

    private val _trailerDataState: MutableLiveData<DataState<VideoResponse>> = MutableLiveData()
    val trailerDataState: MutableLiveData<DataState<VideoResponse>> = _trailerDataState

    private val _creditDataState: MutableLiveData<DataState<CreditResponse>> = MutableLiveData()
    val creditDataState: MutableLiveData<DataState<CreditResponse>> = _creditDataState

    fun getSimilarMovie(movieId: Int) = this.repository.getSimilarMovie(movieId)
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
                is MovieDetailStateEvent.GetCasts -> {
                    repository.getMovieCasts(state.movieId)
                        .onEach {
                            _castDataState.value = it
                        }
                        .launchIn(viewModelScope)
                }
                is MovieDetailStateEvent.GetSimilarMovie -> {
                    _similarMovie =
                        repository.getSimilarMovie(state.movieId).cachedIn(viewModelScope)
                }
                is MovieDetailStateEvent.GetMovieTrailer -> {
                    repository.getMovieTrailer(state.movieId)
                        .onEach {

                            _trailerDataState.value = it
                        }
                        .launchIn(viewModelScope)
                }
                is MovieDetailStateEvent.GetCredit -> {
                    repository.getCredit(state.creditId)
                        .onEach {
                            _creditDataState.value = it
                        }
                        .launchIn(viewModelScope)
                }
            }
        }
    }
}

sealed class MovieDetailStateEvent {

    class GetMovieDetail(val movieId: Int) : MovieDetailStateEvent()
    class GetCasts(val movieId: Int) : MovieDetailStateEvent()
    class GetSimilarMovie(val movieId: Int) : MovieDetailStateEvent()
    class GetMovieTrailer(val movieId: Int) : MovieDetailStateEvent()
    class GetCredit(val creditId: String) : MovieDetailStateEvent()
}