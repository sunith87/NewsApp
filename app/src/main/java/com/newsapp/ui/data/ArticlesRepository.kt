package com.newsapp.ui.data

import com.newsapp.api.GuardianApiService
import com.newsapp.ui.articlelist.model.ArticleDetails
import com.newsapp.ui.articlelist.model.ListItem
import io.reactivex.Single

interface ArticlesRepository {
    fun latestArticles(searchTerm: String = "brexit"): Single<List<ListItem>>

    fun getArticle(
        articleUrl: String,
        requestFields: String = "main,body,headline,thumbnail,sectionName"
    ): Single<ArticleDetails>
}

class GuardianArticlesRepository(
    private val guardianApiService: GuardianApiService,
    private val articleMapper: ArticleMapper,
    private val articleDetailsMapper: ArticleDetailsMapper
) : ArticlesRepository {
    override fun latestArticles(searchTerm: String): Single<List<ListItem>> {
        return guardianApiService.searchArticles(searchTerm)
            .map { response -> articleMapper.map(response) }
    }

    override fun getArticle(articleUrl: String, requestFields: String): Single<ArticleDetails> {
        return guardianApiService.getArticle(articleUrl, requestFields)
            .map { response -> articleDetailsMapper.mapToDetails(response) }
    }
}