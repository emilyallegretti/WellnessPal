package com.bignerdranch.android.wellnesspal.models

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Keep
data class ResponseData(
    @Json(name = "Article") val articles: List<Article>)
{

}