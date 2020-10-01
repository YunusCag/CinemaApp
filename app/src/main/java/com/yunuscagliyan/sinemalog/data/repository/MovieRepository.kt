package com.yunuscagliyan.sinemalog.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.yunuscagliyan.sinemalog.data.api.POST_PER_PAGE
import com.yunuscagliyan.sinemalog.data.api.TheMovieDBInterface
import com.yunuscagliyan.sinemalog.data.source.PopularDataSource
import com.yunuscagliyan.sinemalog.data.source.UpComingDataSource
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
}