package com.yunuscagliyan.sinemalog.ui.category

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.yunuscagliyan.sinemalog.data.models.Genre
import com.yunuscagliyan.sinemalog.data.repository.MovieRepository
import com.yunuscagliyan.sinemalog.utils.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class CategoryViewModel @ViewModelInject constructor(
    private val repository: MovieRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _genreListDataState: MutableLiveData<DataState<List<Genre>>> = MutableLiveData()
    val genreListDataState: LiveData<DataState<List<Genre>>>
        get() = _genreListDataState

    fun setStateEvent(state: CategoryEventState) {
        viewModelScope.launch {
            when (state) {
                is CategoryEventState.GetGenreList -> {
                    repository.getGenresList()
                        .onEach {
                            _genreListDataState.value = it
                        }
                        .launchIn(viewModelScope)
                }
            }
        }
    }

    private val currentQuery = MutableLiveData(DEFAULT_QUERY)
    val genreMovies = currentQuery.switchMap { currentQuery ->
        repository.getMovieByGenre(currentQuery).cachedIn(viewModelScope)
    }

    fun getGenreMovies(genreId: Int) {
        currentQuery.value = genreId
    }

    companion object {
        private const val DEFAULT_QUERY:Int = -1
    }


}

sealed class CategoryEventState {
    object GetGenreList : CategoryEventState()
}