package com.newsapp.ui.articlelist.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.newsapp.R
import com.newsapp.ui.articlelist.ArticleClickListener
import com.newsapp.ui.articlelist.model.ListItem
import com.newsapp.ui.articlelist.model.Article
import com.newsapp.ui.articlelist.model.ArticleWeekData
import java.util.ArrayList

class ArticlesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val ARTICLE_FIRST = 1
        private const val ARTICLE = 2
        private const val WEEK = 3
    }

    private val articles = ArrayList<ListItem>()
    private var articleClickListener: ArticleClickListener? = null

    override fun getItemViewType(position: Int): Int {
        val listItem = articles[position]
        return if (listItem.isWeekData()) {
            WEEK
        } else if (listItem is Article && listItem.firstArticleInWeek) {
            ARTICLE_FIRST
        } else {
            ARTICLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ARTICLE) {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.list_item_article, parent, false)
            ArticleViewHolder(view, articleClickListener)
        } else if (viewType == ARTICLE_FIRST) {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.list_item_article_first, parent, false)
            ArticleFirstViewHolder(view, articleClickListener)
        } else {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.list_item_week, parent, false)
            ArticleWeekViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val articleData = articles[position]
        if (holder is ArticleViewHolder && articleData is Article) {
            holder.bind(articleData)
        } else if (holder is ArticleFirstViewHolder && articleData is Article) {
            holder.bind(articleData)
        } else if (holder is ArticleWeekViewHolder && articleData is ArticleWeekData) {
            holder.bind(articleData.headline)
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun showArticles(articles: List<ListItem>) {
        this.articles.addAll(articles)
        notifyDataSetChanged()
    }

    fun setArticleClickListener(articleClickListener: ArticleClickListener) {
        this.articleClickListener = articleClickListener
    }
}
