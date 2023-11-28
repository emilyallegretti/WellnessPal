package com.bignerdranch.android.wellnesspal
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.wellnesspal.databinding.ArticleLayoutBinding
import com.bignerdranch.android.wellnesspal.models.Article


private const val TAG = "ArticleAdapter.kt"

class ArticleViewHolder(private val binding: ArticleLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(article: Article) {
        binding.articleTitleTextView.text = article.title
        binding.articleAbstractTextView.text = article.abstract
        binding.articleLinkTextView.text = article.url

    }
}

class ArticleAdapter() :
    RecyclerView.Adapter<ArticleViewHolder>() {

    private var articles: MutableList<Article> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ArticleLayoutBinding.inflate(inflater, parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun setArticles(newArticles: List<Article>) {
        articles = newArticles.toMutableList()
    }
}
