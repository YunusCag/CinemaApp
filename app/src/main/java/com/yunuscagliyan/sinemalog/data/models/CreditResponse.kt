package com.yunuscagliyan.sinemalog.data.models


import com.google.gson.annotations.SerializedName

data class CreditResponse(
    @SerializedName("credit_type")
    var creditType: String?,
    @SerializedName("department")
    var department: String?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("job")
    var job: String?,
    @SerializedName("media")
    var media: Media?,
    @SerializedName("media_type")
    var mediaType: String?,
    @SerializedName("person")
    var person: Person?
) {
    data class Media(
        @SerializedName("adult")
        var adult: Boolean?,
        @SerializedName("backdrop_path")
        var backdropPath: String?,
        @SerializedName("character")
        var character: String?,
        @SerializedName("genre_ids")
        var genreİds: List<Int?>?,
        @SerializedName("id")
        var id: Int?,
        @SerializedName("original_language")
        var originalLanguage: String?,
        @SerializedName("original_title")
        var originalTitle: String?,
        @SerializedName("overview")
        var overview: String?,
        @SerializedName("popularity")
        var popularity: Double?,
        @SerializedName("poster_path")
        var posterPath: String?,
        @SerializedName("release_date")
        var releaseDate: String?,
        @SerializedName("title")
        var title: String?,
        @SerializedName("video")
        var video: Boolean?,
        @SerializedName("vote_average")
        var voteAverage: Double?,
        @SerializedName("vote_count")
        var voteCount: Int?
    )

    data class Person(
        @SerializedName("adult")
        var adult: Boolean?,
        @SerializedName("gender")
        var gender: Int?,
        @SerializedName("id")
        var id: Int?,
        @SerializedName("known_for")
        var knownFor: List<KnownFor?>?,
        @SerializedName("known_for_department")
        var knownForDepartment: String?,
        @SerializedName("name")
        var name: String?,
        @SerializedName("popularity")
        var popularity: Double?,
        @SerializedName("profile_path")
        var profilePath: String?
    ) {
        data class KnownFor(
            @SerializedName("adult")
            var adult: Boolean?,
            @SerializedName("backdrop_path")
            var backdropPath: String?,
            @SerializedName("first_air_date")
            var firstAirDate: String?,
            @SerializedName("genre_ids")
            var genreİds: List<Int?>?,
            @SerializedName("id")
            var id: Int?,
            @SerializedName("media_type")
            var mediaType: String?,
            @SerializedName("name")
            var name: String?,
            @SerializedName("origin_country")
            var originCountry: List<String?>?,
            @SerializedName("original_language")
            var originalLanguage: String?,
            @SerializedName("original_name")
            var originalName: String?,
            @SerializedName("original_title")
            var originalTitle: String?,
            @SerializedName("overview")
            var overview: String?,
            @SerializedName("popularity")
            var popularity: Double?,
            @SerializedName("poster_path")
            var posterPath: String?,
            @SerializedName("release_date")
            var releaseDate: String?,
            @SerializedName("title")
            var title: String?,
            @SerializedName("video")
            var video: Boolean?,
            @SerializedName("vote_average")
            var voteAverage: Double?,
            @SerializedName("vote_count")
            var voteCount: Int?
        )
    }
}