package com.yunuscagliyan.sinemalog.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.yunuscagliyan.sinemalog.data.api.POST_PER_PAGE
import com.yunuscagliyan.sinemalog.data.api.TheMovieDBInterface
import com.yunuscagliyan.sinemalog.data.api.getLanguage
import com.yunuscagliyan.sinemalog.data.models.Genre
import com.yunuscagliyan.sinemalog.data.models.GenresResponse
import com.yunuscagliyan.sinemalog.data.models.MovieDetail
import com.yunuscagliyan.sinemalog.data.source.GenreMovieDataSource
import com.yunuscagliyan.sinemalog.data.source.PopularDataSource
import com.yunuscagliyan.sinemalog.data.source.TrendingDataSource
import com.yunuscagliyan.sinemalog.data.source.UpComingDataSource
import com.yunuscagliyan.sinemalog.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val theMovieDBInterface: TheMovieDBInterface
) {
    fun getUpComingMovie()=Pager(
        config = PagingConfig(
            pageSize = POST_PER_PAGE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            UpComingDataSource(theMovieDBInterface)
        }
    ).liveData

    fun getPopularMovie()=Pager(
        config = PagingConfig(
            pageSize = POST_PER_PAGE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            PopularDataSource(theMovieDBInterface)
        }
    ).liveData
    fun getMovieByGenre(genreId:Int)=Pager(
        config = PagingConfig(
            pageSize = POST_PER_PAGE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            GenreMovieDataSource(theMovieDBInterface,genreId)
        }
    ).liveData

    fun getTrendingMovie()=Pager(
        config = PagingConfig(
            pageSize = POST_PER_PAGE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            TrendingDataSource(theMovieDBInterface)
        }
    ).liveData

    suspend fun getMovieDetail(id:Int): Flow<DataState<MovieDetail>> =flow {
        emit(DataState.Loading)

        try{
            val movieDetail=theMovieDBInterface.getMovieDetail(id, getLanguage())
            emit(DataState.Success(movieDetail))
        }catch (e:Exception){
            emit(DataState.Error(e))
        }
    }
    suspend fun getGenresList():Flow<DataState<List<Genre>>> =flow{
        emit(DataState.Loading)
        try{
            val genreList=theMovieDBInterface.getGenreList(getLanguage())
            emit(DataState.Success(genreList.genres))
        }catch (e:java.lang.Exception){
            emit(DataState.Error(e))
        }
    }
}