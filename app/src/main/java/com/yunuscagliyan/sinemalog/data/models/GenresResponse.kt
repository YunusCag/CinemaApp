package com.yunuscagliyan.sinemalog.data.models


import com.google.gson.annotations.SerializedName

data class GenresResponse(
    @SerializedName("genres")
    var genres: List<Genre>
)
