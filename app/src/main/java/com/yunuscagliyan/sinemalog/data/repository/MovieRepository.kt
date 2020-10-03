package com.yunuscagliyan.sinemalog.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.yunuscagliyan.sinemalog.data.api.POST_PER_PAGE
import com.yunuscagliyan.sinemalog.data.api.TheMovieDBInterface
import com.yunuscagliyan.sinemalog.data.models.MovieDetail
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
            val movieDetail=theMovieDBInterface.getMovieDetail(id,"en-US")
            emit(DataState.Success(movieDetail))
        }catch (e:Exception){
            emit(DataState.Error(e))
        }
    }
}