package com.newsapp.ui.articlelist

import com.newsapp.ui.articlelist.model.Article

interface ArticleClickListener {
    fun onClicked(article: Article)
}