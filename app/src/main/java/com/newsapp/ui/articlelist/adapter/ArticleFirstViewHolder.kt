package com.newsapp.ui.articlelist.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.newsapp.R
import com.newsapp.ui.articlelist.ArticleClickListener
import com.newsapp.ui.articlelist.model.Article

internal class ArticleFirstViewHolder(
    view: View,
    private val articleClickListener: ArticleClickListener?
) : RecyclerView.ViewHolder(view), View.OnClickListener {

    private var article: Article? = null


    init {
        view.setOnClickListener(this)
    }

    fun bind(article: Article) {
        this.article = article
        itemView.findViewById<TextView>(R.id.article_first_headline_textview).text = article.title
        itemView.findViewById<TextView>(R.id.article_first_date_textview).text = article.publishedFormatted

        Glide.with(itemView.context)
            .load(article.thumbnail)
            .asBitmap()
            .centerCrop()
            .into(itemView.findViewById<ImageView>(R.id.article_first_thumbnail_imageview))
    }

    override fun onClick(v: View) {
        article?.apply {
            articleClickListener?.onClicked(this)
        }
    }
}