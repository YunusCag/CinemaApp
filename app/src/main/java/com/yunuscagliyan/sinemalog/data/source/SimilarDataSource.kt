package com.yunuscagliyan.sinemalog.data.source

import android.util.Log
import androidx.paging.PagingSource
import com.yunuscagliyan.sinemalog.data.api.FIRST_PAGE
import com.yunuscagliyan.sinemalog.data.api.TheMovieDBInterface
import com.yunuscagliyan.sinemalog.data.api.getLanguage
import com.yunuscagliyan.sinemalog.data.models.Movie
import retrofit2.HttpException
import java.io.IOException

class SimilarDataSource(
    private val theMovieDBInterface: TheMovieDBInterface,
    private val movieId:Int
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: FIRST_PAGE
        return try {
            val response = theMovieDBInterface.getSimilarMovie(movieId,position, getLanguage())
            val movies = response.movies
            LoadResult.Page(
                data = movies,
                prevKey = if (position == FIRST_PAGE) null else position - 1,
                nextKey = if (movies.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

}