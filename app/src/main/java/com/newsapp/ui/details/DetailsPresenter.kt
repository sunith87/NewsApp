package com.newsapp.ui.details

import com.newsapp.base.BasePresenter
import com.newsapp.base.BaseView
import com.newsapp.base.SchedulerProvider
import com.newsapp.ui.articlelist.model.ArticleDetails
import com.newsapp.ui.articlelist.model.error.ArticleFetchError
import com.newsapp.ui.data.ArticlesRepository
import io.reactivex.disposables.CompositeDisposable

class DetailsPresenter(
    private val articlesRepository: ArticlesRepository,
    private val schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable = CompositeDisposable()
) : BasePresenter<DetailsPresenterView>(compositeDisposable) {

    fun register(articleUrl: String, view: DetailsPresenterView) {
        super.register(view)
        addDisposable(
            articlesRepository.getArticle(articleUrl)
                .subscribeOn(schedulerProvider.ioScheduler())
                .observeOn(schedulerProvider.mainScheduler())
                .subscribe({ articleDetails -> view.renderDetails(articleDetails) },
                    { error -> view.showError(ArticleFetchError(error)) })
        )
    }
}

interface DetailsPresenterView : BaseView {
    fun renderDetails(articleDetails: ArticleDetails)
    fun showError(articleDetailsError: ArticleFetchError)
}