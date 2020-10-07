package com.yunuscagliyan.sinemalog.data.models


import com.google.gson.annotations.SerializedName

data class CastResponse(
    @SerializedName("cast")
    var cast: List<Cast?>?,
    @SerializedName("crew")
    var crew: List<Crew?>?,
    @SerializedName("id")
    var id: Int?
) {
}