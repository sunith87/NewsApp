package com.newsapp.ui.details

import com.newsapp.base.BasePresenter
import com.newsapp.base.BaseView
import com.newsapp.base.SchedulerProvider
import com.newsapp.ui.articlelist.model.ArticleDetails
import com.newsapp.ui.articlelist.model.error.ArticleFetchError
import com.newsapp.ui.data.ArticlesRepository
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailsPresenter(
    private val articlesRepository: ArticlesRepository,
    private val schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable = CompositeDisposable()
) : BasePresenter<DetailsPresenterView>(compositeDisposable) {

    private var getArticleJob: Job? = null

    fun register(articleUrl: String, view: DetailsPresenterView) {
        super.register(view)
        getArticleJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                view.renderDetails(articlesRepository.getArticle(articleUrl))
            } catch (ex: Throwable) {
                view.showError(ArticleFetchError(ex))
            }

        }
    }

    override fun unregister() {
        super.unregister()
        getArticleJob?.cancel()
    }
}

interface DetailsPresenterView : BaseView {
    fun renderDetails(articleDetails: ArticleDetails)
    fun showError(articleDetailsError: ArticleFetchError)
}