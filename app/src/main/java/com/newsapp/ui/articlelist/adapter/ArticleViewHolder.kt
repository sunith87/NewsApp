package com.newsapp.ui.articlelist.adapter

import android.graphics.Bitmap
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.newsapp.R
import com.newsapp.ui.articlelist.ArticleClickListener
import com.newsapp.ui.articlelist.model.Article

internal class ArticleViewHolder(
    view: View,
    private val articleClickListener: ArticleClickListener?
) : RecyclerView.ViewHolder(view), View.OnClickListener {

    private var article: Article? = null

    private val roundedTarget: BitmapImageViewTarget
        get() = object : BitmapImageViewTarget(getThumbnailImageView()) {
            override fun setResource(resource: Bitmap) {
                val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(itemView.resources, resource)
                roundedBitmapDrawable.isCircular = true
                getThumbnailImageView().setImageDrawable(roundedBitmapDrawable)
            }
        }

    private fun getThumbnailImageView() = itemView.findViewById<ImageView>(R.id.article_thumbnail_imageview)

    init {
        view.setOnClickListener(this)
    }

    fun bind(article: Article) {
        this.article = article
        itemView.findViewById<TextView>(R.id.article_details_headline_textview).text = article.title
        itemView.findViewById<TextView>(R.id.article_details_date_textview).text = article.publishedFormatted

        Glide.with(itemView.context)
            .load(article.thumbnail)
            .asBitmap()
            .centerCrop()
            .into(roundedTarget)
    }

    override fun onClick(v: View) {
        article?.apply {
            articleClickListener?.onClicked(this)
        }
    }
}