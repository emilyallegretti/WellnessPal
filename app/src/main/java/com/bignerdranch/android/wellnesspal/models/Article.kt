package com.bignerdranch.android.wellnesspal.models


import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Keep
data class Article(
    @Json(name = "title") var title: String = "",
    @Json(name = "abstract") var abstract: String = "",
    @Json(name = "url") var url: String = "",
    @Json(name = "byline") var byline: String = "") {

}
