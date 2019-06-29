package com.newsapp.ui.data

import com.newsapp.api.model.RawArticle
import com.newsapp.api.model.RawArticleDetailsResponse
import com.newsapp.api.model.RawArticleListResponse
import com.newsapp.ui.articlelist.model.Article
import com.newsapp.ui.articlelist.model.ArticleDetails
import com.newsapp.ui.articlelist.model.ArticleWeekData
import com.newsapp.ui.articlelist.model.ListItem
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ArticleMapper {
    private val DATE_FORMAT = "dd/MM/yyyy"
    private val DAY_IN_MILLIS = 1000 * 60 * 60 * 24

    fun map(rawArticleListResponse: RawArticleListResponse): List<ListItem> {
        val articles = ArrayList<ListItem>()
        val currentDate = Date()
        var currentWeek = 1

        val results = rawArticleListResponse.response.results
        sortDatesFromCurrentToEarlier(results)

        addCurrentWeek(articles, currentWeek)
        for (rawArticle in results) {
            val articleDate = rawArticle.webPublicationDate

            if (articleNotFromCurrentWeek(currentWeek, articleDate, currentDate)) {
                currentWeek++
                addCurrentWeek(articles, currentWeek)
            }

            articles.add(
                Article(
                    rawArticle.id,
                    rawArticle.fields.thumbnail,
                    rawArticle.sectionId,
                    rawArticle.sectionName,
                    getFormattedDate(articleDate),
                    rawArticle.fields.headline,
                    rawArticle.apiUrl
                )
            )
        }

        return articles
    }

    private fun sortDatesFromCurrentToEarlier(results: List<RawArticle>) {
        Collections.sort<RawArticle>(results) { article1, article2 ->
            article2.webPublicationDate.compareTo(article1.webPublicationDate)
        }
    }

    private fun addCurrentWeek(articles: MutableList<ListItem>, currentWeek: Int) {
        articles.add(ArticleWeekData(currentWeek))
    }

    private fun articleNotFromCurrentWeek(currentWeek: Int, articleDate: Date, currentDate: Date): Boolean {
        val diffInMillis = currentDate.time - articleDate.time
        val diffInDays = diffInMillis / DAY_IN_MILLIS
        return currentWeek * 7 < diffInDays
    }

    private fun getFormattedDate(webPublicationDate: Date): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.ROOT)
        return dateFormat.format(webPublicationDate)
    }

    fun mapToDetails(articleDetailsResponse: RawArticleDetailsResponse): ArticleDetails {
        val fields = articleDetailsResponse.response.content.fields
        return ArticleDetails(
            fields.main,
            fields.body,
            fields.headline,
            fields.thumbnail
        )
    }
}