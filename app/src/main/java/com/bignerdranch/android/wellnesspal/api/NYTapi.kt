package com.bignerdranch.android.wellnesspal.api
import com.bignerdranch.android.wellnesspal.models.ArticleResponse
import retrofit2.Call
import retrofit2.http.GET
interface NYTapi {

    @GET("health.json?api-key=Hq96UxzswJ2OTNkC8CV6efbsnAuUURFK")
    fun getArticles(): Call<ArticleResponse>


}