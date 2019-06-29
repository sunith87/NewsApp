package com.newsapp.ui.articlelist.model

import java.io.Serializable

data class Article(
    val id: String,
    val thumbnail: String,
    val sectionId: String,
    val sectionName: String,
    val publishedFormatted: String,
    val title: String,
    val url: String
) : ListItem, Serializable {
    override fun isWeekData(): Boolean = false
}