package com.newsapp.ui.articlelist

import com.newsapp.base.SchedulerProvider
import com.newsapp.ui.articlelist.model.Article
import com.newsapp.ui.articlelist.model.ListItem
import com.newsapp.ui.articlelist.model.error.ArticleFetchError
import com.newsapp.ui.data.ArticlesRepository
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

import org.mockito.MockitoAnnotations.initMocks

class ArticleListPresenterTest {
    @Mock
    private lateinit var mockSchedulerProvider: SchedulerProvider
    @Mock
    private lateinit var articlesRepository: ArticlesRepository
    @Mock
    private lateinit var view: ArticleListView
    @Mock
    private lateinit var mockArticle: Article
    @Mock
    private lateinit var mockArticleDataList: List<ListItem>

    private lateinit var presenter: ArticleListPresenter

    @Before
    @Throws(Exception::class)
    fun setUp() {
        initMocks(this)
        whenever(mockSchedulerProvider.ioScheduler()).thenReturn(Schedulers.trampoline())
        whenever(mockSchedulerProvider.mainScheduler()).thenReturn(Schedulers.trampoline())
        whenever(view.onRefreshAction()).thenReturn(Observable.just(Object()))
        whenever(view.onArticleClicked()).thenReturn(Observable.just(mockArticle))
        presenter = ArticleListPresenter(
            articlesRepository,
            mockSchedulerProvider
        )
    }

    @Test
    @Throws(Exception::class)
    fun register_shouldFetchLatestArticles() {
        presenter.register(view)

        Mockito.verify(articlesRepository).latestArticles()
    }

    @Test
    fun register_shouldShowArticles_whenFetchSuccessful() {
        whenever(articlesRepository.latestArticles()).thenReturn(Single.just(mockArticleDataList))

        presenter.register(view)

        Mockito.verify(articlesRepository).latestArticles()
        Mockito.verify(view).showRefreshing(false)
        Mockito.verify(view).showArticles(mockArticleDataList)
    }

    @Test
    fun register_shouldShowError_whenFetchFails() {
        val expectedThrowable = Throwable()
        whenever(articlesRepository.latestArticles()).thenReturn(Single.error(expectedThrowable))
        val argumentCaptor = argumentCaptor<ArticleFetchError>()

        presenter.register(view)

        Mockito.verify(articlesRepository).latestArticles()
        Mockito.verify(view).showRefreshing(false)
        Mockito.verify(view).handlerError(argumentCaptor.capture())

        assertEquals(expectedThrowable, argumentCaptor.firstValue.throwable)
    }

    @Test
    fun register_onArticleClicked() {
        whenever(view.onArticleClicked()).thenReturn(Observable.just(mockArticle))

        presenter.register(view)

        Mockito.verify(view).openArticleDetail(mockArticle)
    }
}