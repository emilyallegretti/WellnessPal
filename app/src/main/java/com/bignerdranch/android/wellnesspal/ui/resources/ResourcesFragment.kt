package com.bignerdranch.android.wellnesspal.ui.resources

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bignerdranch.android.wellnesspal.databinding.FragmentResourcesBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.wellnesspal.ArticleAdapter
import com.bignerdranch.android.wellnesspal.api.NYTapi
import com.bignerdranch.android.wellnesspal.models.Article
import com.bignerdranch.android.wellnesspal.models.ResponseData
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

    private var _binding: FragmentResourcesBinding? = null
    private lateinit var adapter: ArticleAdapter


    private val binding get() = _binding!!
    private lateinit var resourcesViewModel: ResourcesViewModel


    private lateinit var nyt: NYTapi
    private var articles: MutableList<Article> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        resourcesViewModel = ViewModelProvider(this).get(ResourcesViewModel::class.java)

        _binding = FragmentResourcesBinding.inflate(inflater, container, false)
        binding.articlesRecyclerView.layoutManager = LinearLayoutManager(context)


        val root: View = binding.root


        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        nyt = retrofit.create(NYTapi::class.java)

        // Fetch articles when the fragment is created

        this.adapter = ArticleAdapter()
        Log.d(TAG, "Resources: Setting adapter")
        binding.articlesRecyclerView.adapter = adapter

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val call = nyt.getArticles()

        call.enqueue(object : Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                Log.d(TAG, "$response")
                if (response.isSuccessful) {
                    Log.d(TAG, "API call was successful")

                    val articleResponse = response.body()
                    articleResponse?.let {
                        articles.clear()
                        articles = it.results.toMutableList()
                        adapter.setArticles(articles)
                        binding.articlesRecyclerView.adapter?.notifyDataSetChanged()
                        //Log.d(TAG, "Articles retrieved: $articles")
                        Log.d(TAG, "Article one: ${articles[1]}" )
                    }

                } else {
                    Log.d(TAG, "Failed to fetch articles")
                }
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                Log.d(TAG, "Network error: "+ t.message)
            }
        })




        }

    /*private fun fetchArticles() {
        val call = nyt.getArticles()

        call.enqueue(object : Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                Log.d(TAG, "$response")
                if (response.isSuccessful) {
                    Log.d(TAG, "API call was successful")

                    val articleResponse = response.body()
                    articleResponse?.let {
                        articles.clear()
                        articles = it.results.toMutableList()

                        binding.articlesRecyclerView.adapter?.notifyDataSetChanged()
                        Log.d(TAG, "Articles retrieved: $articles")
                    }
                } else {
                    Log.d(TAG, "Failed to fetch articles")
                }
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                Log.d(TAG, "Network error: "+ t.message)
            }
        })
    }*/


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d(TAG, "Resources: OnDestroyView() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Resources: OnDestroy() called")
    }

}