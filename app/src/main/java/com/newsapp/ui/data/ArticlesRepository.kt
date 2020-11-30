package com.newsapp.ui.data

import com.newsapp.api.GuardianApiService
import com.newsapp.ui.articlelist.model.ArticleDetails
import com.newsapp.ui.articlelist.model.ListItem
import io.reactivex.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface ArticlesRepository {
    companion object {
        const val DEFAULT_SEARCH_TERM: String = "football,f1,cricket,sports"
    }

    suspend fun latestArticlesAsync(searchTerm: String = DEFAULT_SEARCH_TERM): List<ListItem>

    suspend fun getArticle(
        articleUrl: String,
        requestFields: String = "main,body,headline,thumbnail,sectionName"
    ): ArticleDetails
}

class GuardianArticlesRepository(
    private val guardianApiService: GuardianApiService,
    private val articleMapper: ArticleMapper,
    private val articleDetailsMapper: ArticleDetailsMapper
) : ArticlesRepository {
    override suspend fun latestArticlesAsync(searchTerm: String): List<ListItem> {
        val searchArticles = guardianApiService.searchArticles(searchTerm)
        val response = searchArticles.await()
        return articleMapper.map(response)
    }

    override suspend fun getArticle(articleUrl: String, requestFields: String): ArticleDetails {
        return articleDetailsMapper.mapToDetails(guardianApiService.getArticle(articleUrl, requestFields).await())
    }
}