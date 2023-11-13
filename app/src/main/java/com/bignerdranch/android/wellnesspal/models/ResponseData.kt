package com.bignerdranch.android.wellnesspal.models

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Keep
class ResponseData(){
    @Json(name = "results") lateinit var results: List<Article>
}

