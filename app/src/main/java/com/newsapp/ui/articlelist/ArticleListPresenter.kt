package com.newsapp.ui.articlelist

import com.newsapp.base.BasePresenter
import com.newsapp.base.BaseView
import com.newsapp.base.SchedulerProvider
import com.newsapp.ui.data.ArticlesRepository
import com.newsapp.ui.articlelist.model.Article
import com.newsapp.ui.articlelist.model.ListItem
import com.newsapp.ui.articlelist.model.error.ArticleFetchError
import io.reactivex.Observable

class ArticleListPresenter(
    private val articlesRepository: ArticlesRepository,
    private val schedulerProvider: SchedulerProvider
) : BasePresenter<ArticleListView>() {

    override fun register(view: ArticleListView) {
        super.register(view)
        fetchData(view)
        handleArticleClick(view)
    }

    private fun fetchData(view: ArticleListView) {
        addDisposable(
            view.onRefreshAction()
                .doOnNext { view.showRefreshing(true) }
                .flatMapSingle {
                    articlesRepository.latestArticles().subscribeOn(schedulerProvider.ioScheduler())
                }
                .observeOn(schedulerProvider.mainScheduler())
                .subscribeOn(schedulerProvider.ioScheduler())
                .subscribe({ articles ->
                    view.showRefreshing(false)
                    view.showArticles(articles)
                }, { error ->
                    view.showRefreshing(false)
                    view.handlerError(ArticleFetchError(error))
                })
        )
    }

    private fun handleArticleClick(view: ArticleListView) {
        addDisposable(
            view.onArticleClicked()
                .subscribe { article -> view.openArticleDetail(article) }
        )
    }
}

interface ArticleListView : BaseView {

    // actions
    fun onArticleClicked(): Observable<Article>
    fun onRefreshAction(): Observable<Any>

    // responses
    fun showRefreshing(isRefreshing: Boolean)
    fun showArticles(articles: List<ListItem>)
    fun openArticleDetail(article: Article)
    fun handlerError(error: ArticleFetchError)
}
