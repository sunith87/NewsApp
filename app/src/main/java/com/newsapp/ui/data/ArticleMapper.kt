package com.newsapp.ui.data

import com.newsapp.api.model.RawArticle
import com.newsapp.api.model.RawArticleListResponse
import com.newsapp.ui.articlelist.model.Article
import com.newsapp.ui.articlelist.model.ArticleWeekData
import com.newsapp.ui.articlelist.model.ListItem
import com.newsapp.util.DateFormatUtil
import java.util.*
import kotlin.collections.ArrayList

class ArticleMapper(private val dateFormatUtil: DateFormatUtil) {
    private val DAY_IN_MILLIS = 1000 * 60 * 60 * 24

    fun map(rawArticleListResponse: RawArticleListResponse): List<ListItem> {
        val articles = ArrayList<ListItem>()
        val currentDate = Date()
        var currentWeek = 1
        var firstArticleInWeek = true

        val results = rawArticleListResponse.response.results
        sortDatesFromCurrentToEarlier(results)

        addCurrentWeek(articles, currentWeek)
        for (rawArticle in results) {
            val articleDate = rawArticle.webPublicationDate

            val articleNotFromCurrentWeek = articleNotFromCurrentWeek(currentWeek, articleDate, currentDate)
            if (articleNotFromCurrentWeek) {
                currentWeek++
                addCurrentWeek(articles, currentWeek)
                firstArticleInWeek = true
            }

            addArticle(articles, rawArticle, articleDate, firstArticleInWeek)

            if (firstArticleInWeek) {
                firstArticleInWeek = false
            }
        }

        return articles
    }

    private fun addArticle(
        articles: ArrayList<ListItem>,
        rawArticle: RawArticle,
        articleDate: Date,
        firstArticleInWeek: Boolean
    ) {
        articles.add(
            Article(
                rawArticle.id,
                rawArticle.fields.thumbnail,
                rawArticle.sectionId,
                rawArticle.sectionName,
                dateFormatUtil.getFormattedDate(articleDate),
                rawArticle.fields.headline,
                rawArticle.apiUrl,
                firstArticleInWeek
            )
        )
    }

    private fun sortDatesFromCurrentToEarlier(results: List<RawArticle>) {
        Collections.sort(results) { article1, article2 ->
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
}