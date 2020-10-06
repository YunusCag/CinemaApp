package com.yunuscagliyan.sinemalog.data.source

import androidx.paging.PagingSource
import com.yunuscagliyan.sinemalog.data.api.FIRST_PAGE
import com.yunuscagliyan.sinemalog.data.api.TheMovieDBInterface
import com.yunuscagliyan.sinemalog.data.api.getLanguage
import com.yunuscagliyan.sinemalog.data.models.Movie
import retrofit2.HttpException
import java.io.IOException

class GenreMovieDataSource(
    private val theMovieDBInterface: TheMovieDBInterface,
    private val genreId:Int
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, Movie> {
        val position = params.key ?: FIRST_PAGE
        return try {
            val response = theMovieDBInterface.getMovieByGenre(position, getLanguage(), "US",genreId)
            val movies = response.movies

            PagingSource.LoadResult.Page(
                data = movies,
                prevKey = if (position == FIRST_PAGE) null else position - 1,
                nextKey = if (movies.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            PagingSource.LoadResult.Error(exception)
        } catch (exception: HttpException) {
            PagingSource.LoadResult.Error(exception)
        }
    }
}