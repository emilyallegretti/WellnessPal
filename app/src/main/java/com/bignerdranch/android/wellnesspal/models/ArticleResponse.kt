package com.bignerdranch.android.wellnesspal.models

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Keep
data class ArticleResponse(
    @Json(name = "response") var response: ResponseData)
    {

}