package com.newsapp.ui.articlelist.model

data class ArticleWeekData(val headline: Int) : ListItem {
    override fun isWeekData(): Boolean = true
}