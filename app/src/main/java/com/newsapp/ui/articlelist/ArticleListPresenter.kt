package com.newsapp.ui.articlelist

import com.newsapp.base.BasePresenter
import com.newsapp.base.BaseView
import com.newsapp.base.SchedulerProvider
import com.newsapp.ui.data.ArticlesRepository
import com.newsapp.ui.articlelist.model.Article
import com.newsapp.ui.articlelist.model.ListItem
import com.newsapp.ui.articlelist.model.error.ArticleFetchError
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class ArticleListPresenter(
    private val articlesRepository: ArticlesRepository,
    private val schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable = CompositeDisposable()
) : BasePresenter<ArticleListView>(compositeDisposable) {

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
        addDisposable(
            Observable.fromCallable { view?.also { it.showRefreshing(true) } }
                .flatMapSingle {
                    articlesRepository.latestArticles()
                }
                .observeOn(schedulerProvider.mainScheduler())
                .subscribeOn(schedulerProvider.ioScheduler())
                .subscribe({ articles ->
                    handleSuccess(articles)
                }, { error ->
                    handleError(error)
                })
        )
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
