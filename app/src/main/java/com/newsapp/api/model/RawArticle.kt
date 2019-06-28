package com.newsapp.api.model

import java.util.*

data class RawArticle(
        val id: String,
        val sectionId: String,
        val sectionName: String,
        val webPublicationDate: Date,
        val webTitle: String,
        val webUrl: String,
        val apiUrl: String,
        val fields: RawArticleFields
) 

