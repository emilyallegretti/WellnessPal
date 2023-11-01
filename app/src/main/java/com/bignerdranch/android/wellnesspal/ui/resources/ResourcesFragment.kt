package com.bignerdranch.android.wellnesspal.ui.resources

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bignerdranch.android.wellnesspal.databinding.FragmentResourcesBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.wellnesspal.api.NYTapi
import com.bignerdranch.android.wellnesspal.models.Article
import com.bignerdranch.android.wellnesspal.models.ArticleResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val BASE_URL = "https://api.nytimes.com/svc/topstories/v2/"


private const val TAG = "ResourcesFragment"

class ResourcesFragment : Fragment() {

    private lateinit var _binding: FragmentResourcesBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var nyt: NYTapi
    private val articles: MutableList<Article> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        _binding = FragmentResourcesBinding.inflate(inflater, container, false)
        val view = _binding.root

        recyclerView = _binding.articlesRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ArticleAdapter(articles)
        recyclerView.adapter = adapter

        val refreshButton = _binding.articlesRefreshButton
        refreshButton.setOnClickListener { refreshArticles() }

        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        nyt = retrofit.create(NYTapi::class.java)
        Log.d(TAG, "$nyt")

        // Fetch articles when the fragment is created
        fetchArticles()

        return view
    }

    private fun fetchArticles() {
        val call = nyt.getArticles()

        call.enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(call: Call<ArticleResponse>, response: Response<ArticleResponse>) {
                Log.d(TAG, "$response")
                if (response.isSuccessful) {
                    val articleResponse = response.body()
                    articleResponse?.let {
                        articles.clear()
                        articles.addAll(it.response.articles)
                        recyclerView.adapter?.notifyDataSetChanged()
                    }
                } else {
                    Log.d(TAG, "Failed to fetch articles")
                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                Log.d(TAG, "Network error")
            }
        })
    }
    private fun refreshArticles() {
        fetchArticles()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // NEED TO NULL THE BINDING NOT WORKING _binding = null
    }

}