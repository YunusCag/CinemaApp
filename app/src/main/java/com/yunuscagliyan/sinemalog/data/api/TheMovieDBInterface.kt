package com.yunuscagliyan.sinemalog.data.api

import com.yunuscagliyan.sinemalog.data.models.CastResponse
import com.yunuscagliyan.sinemalog.data.models.GenresResponse
import com.yunuscagliyan.sinemalog.data.models.MovieDetail
import com.yunuscagliyan.sinemalog.data.models.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

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

    @GET("movie/top_rated")
    suspend fun getTopRatedMovie(
        @Query("page") page:Int,
        @Query("language") lang: String,
        @Query("region") region: String
    ):MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") id: Int,
        @Query("language") lang: String
    ): MovieDetail

    @GET("genre/movie/list?")
    suspend fun getGenreList(
        @Query("language") lang: String
    ): GenresResponse

    @GET("movie/popular?")
    suspend fun getMovieByGenre(
        @Query("page") page: Int,
        @Query("language") lang: String,
        @Query("region") region: String,
        @Query("with_genres")genreId:Int
    ): MovieResponse


    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCast(
        @Path("movie_id")movieId:Int
    ):CastResponse
}

fun getLanguage(): String {
    return "en-US"
}