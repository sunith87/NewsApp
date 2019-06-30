package com.newsapp.ui.data

import com.newsapp.api.model.RawArticleDetailsResponse
import com.newsapp.ui.articlelist.model.ArticleDetails
import com.newsapp.util.DateFormatUtil

class ArticleDetailsMapper(private val mockDateFormatUtil: DateFormatUtil) {

    fun mapToDetails(articleDetailsResponse: RawArticleDetailsResponse): ArticleDetails {
        val content = articleDetailsResponse.response.content
        val fields = content.fields
        return ArticleDetails(
            fields.main,
            fields.body,
            fields.headline,
            fields.thumbnail,
            content.sectionName,
            mockDateFormatUtil.getFormattedDate(content.webPublicationDate)
        )
    }
}