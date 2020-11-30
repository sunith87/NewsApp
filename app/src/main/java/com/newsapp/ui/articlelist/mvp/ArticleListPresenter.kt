package com.newsapp.ui.articlelist.mvp

import com.newsapp.base.BasePresenter
import com.newsapp.base.BaseView
import com.newsapp.ui.data.ArticlesRepository
import com.newsapp.ui.articlelist.model.Article
import com.newsapp.ui.articlelist.model.ListItem
import com.newsapp.ui.articlelist.model.error.ArticleFetchError
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ArticleListPresenter(
    private val articlesRepository: ArticlesRepository,
    compositeDisposable: CompositeDisposable = CompositeDisposable()
) : BasePresenter<ArticleListView>(compositeDisposable) {

    var refreshJob: Job? = null

    override fun register(view: ArticleListView) {
        super.register(view)
        onRefresh()
        handleArticleClick()
    }

    private fun handleArticleClick() {
        view?.also {
            addDisposable(
                it.onArticleClicked()
                    .subscribe { article -> it.openArticleDetail(article) }
            )
        }
    }

    fun onRefresh() {
        refreshJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                val latestArticles = articlesRepository.latestArticlesAsync()
                handleSuccess(latestArticles)
            } catch (error: Throwable) {
                handleError(error)
            }
        }
    }

    override fun unregister() {
        super.unregister()
        refreshJob?.cancel()
    }

    private fun handleError(error: Throwable) {
        view?.apply {
            showRefreshing(false)
            handlerError(ArticleFetchError(error))
        }
    }

    private fun handleSuccess(articles: List<ListItem>) {
        view?.apply {
            showRefreshing(false)
            showArticles(articles)
        }
    }
}

interface ArticleListView : BaseView {
    fun onArticleClicked(): Observable<Article>

    fun showRefreshing(isRefreshing: Boolean)

    fun showArticles(articles: List<ListItem>)
    fun openArticleDetail(article: Article)
    fun handlerError(error: ArticleFetchError)
}
