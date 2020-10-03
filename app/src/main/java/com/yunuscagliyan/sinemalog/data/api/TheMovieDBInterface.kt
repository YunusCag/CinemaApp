package com.yunuscagliyan.sinemalog.data.api

import android.graphics.Movie
import com.yunuscagliyan.sinemalog.data.models.MovieDetail
import com.yunuscagliyan.sinemalog.data.models.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

const val API_KEY = "4ceba0985010b11eb871640206d56895"
const val BASE_URL = "https://api.themoviedb.org/3/"

const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"
const val FIRST_PAGE = 1
const val POST_PER_PAGE = 20

interface TheMovieDBInterface {

    @GET("movie/upcoming?")
    suspend fun getUpComingMovie(
        @Query("page") page: Int,
        @Query("language") lang: String
    ): MovieResponse

    @GET("movie/popular?")
    suspend fun getPopularMovie(
        @Query("page") page: Int,
        @Query("language") lang: String,
        @Query("region") region: String
    ): MovieResponse

    @GET("trending/movie/day?")
    suspend fun getTrendingMovie(
        @Query("page") page: Int,
        @Query("language") lang: String,
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") id: Int,
        @Query("language") lang: String
    ): MovieDetail

}