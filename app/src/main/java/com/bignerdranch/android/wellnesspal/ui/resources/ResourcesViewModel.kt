package com.bignerdranch.android.wellnesspal.ui.resources

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.wellnesspal.api.NYTapi
import com.bignerdranch.android.wellnesspal.models.Article
import com.bignerdranch.android.wellnesspal.models.ResponseData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val BASE_URL = "https://api.nytimes.com/svc/topstories/v2/"

private const val TAG = "ResourcesViewModel"

class ResourcesViewModel : ViewModel() {

    private lateinit var nyt: NYTapi

    private var articleList = ArrayList<Article>()

    private val _articles = MutableStateFlow(articleList)

    val articles = _articles.asStateFlow()



    fun loadArticles() {

            val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            nyt = retrofit.create(NYTapi::class.java)

            val call = nyt.getArticles()

            call.enqueue(object : Callback<ResponseData> {
                override fun onResponse(
                    call: Call<ResponseData>,
                    response: Response<ResponseData>
                ) {
                    Log.d(TAG, "$response")
                    if (response.isSuccessful) {
                        Log.d(TAG, "API call was successful")

                        val articleResponse = response.body()
                        articleResponse?.let {
                            _articles.value.clear()
                            _articles.value.addAll(articleResponse.results)
                            Log.d(TAG, "Articles are ${_articles.value}")
                        }

                    } else {
                        Log.d(TAG, "Failed to fetch articles")
                    }
                }

                override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                    Log.d(TAG, "Network error: " + t.message)
                }
            })


    }


}