package com.newsapp.ui.articlelist

import com.newsapp.base.SchedulerProvider
import com.newsapp.ui.articlelist.model.Article
import com.newsapp.ui.articlelist.model.ListItem
import com.newsapp.ui.articlelist.model.error.ArticleFetchError
import com.newsapp.ui.articlelist.mvp.ArticleListPresenter
import com.newsapp.ui.articlelist.mvp.ArticleListView
import com.newsapp.ui.data.ArticlesRepository
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mock

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
        whenever(view.onArticleClicked()).thenReturn(Observable.just(mockArticle))
        presenter = ArticleListPresenter(
            articlesRepository
        )
    }

    @Test
    @Throws(Exception::class)
    fun `given register called then should fetch Latest Articles`() {
        presenter.register(view)

        verify(articlesRepository).latestArticlesAsync()
    }

    @Test
    fun `given register called then should should show articles when fetch successful`() {
        whenever(articlesRepository.latestArticlesAsync()).thenReturn(Single.just(mockArticleDataList))

        presenter.register(view)

        verify(articlesRepository).latestArticlesAsync()
        verify(view).showRefreshing(false)
        verify(view).showArticles(mockArticleDataList)
    }

    @Test
    fun `given onRefresh called then should should show articles when fetch successful`() {
        whenever(articlesRepository.latestArticlesAsync()).thenReturn(Single.just(mockArticleDataList))

        presenter.register(view)
        presenter.onRefresh()

        verify(articlesRepository, atLeastOnce()).latestArticlesAsync()
        verify(view, atLeastOnce()).showRefreshing(false)
        verify(view, atLeastOnce()).showArticles(mockArticleDataList)
    }

    @Test
    fun `given register called then should should show error when fetch fails`() {
        val expectedThrowable = Throwable()
        whenever(articlesRepository.latestArticlesAsync()).thenReturn(Single.error(expectedThrowable))
        val argumentCaptor = argumentCaptor<ArticleFetchError>()

        presenter.register(view)

        verify(articlesRepository).latestArticlesAsync()
        verify(view).showRefreshing(false)
        verify(view).handlerError(argumentCaptor.capture())

        assertEquals(expectedThrowable, argumentCaptor.firstValue.throwable)
    }

    @Test
    fun `given an article clicked after view registered then open article called on view`() {
        whenever(view.onArticleClicked()).thenReturn(Observable.just(mockArticle))

        presenter.register(view)

        verify(view).openArticleDetail(mockArticle)
    }
}