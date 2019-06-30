package com.newsapp.ui.details

import com.newsapp.base.SchedulerProvider
import com.newsapp.ui.articlelist.model.ArticleDetails
import com.newsapp.ui.articlelist.model.error.ArticleFetchError
import com.newsapp.ui.data.ArticlesRepository
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

import org.mockito.Mock
import org.mockito.MockitoAnnotations

class DetailsPresenterTest {

    companion object {
        private const val MOCK_ARTICLE_URL = "mock_url"
    }

    private lateinit var presenter: DetailsPresenter
    @Mock
    private lateinit var mockArticleDetails: ArticleDetails
    @Mock
    private lateinit var mockView: DetailsPresenterView
    @Mock
    private lateinit var mockSchedulerProvider: SchedulerProvider

    @Mock
    private lateinit var mockArticlesRepository: ArticlesRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        whenever(mockSchedulerProvider.ioScheduler()).thenReturn(Schedulers.trampoline())
        whenever(mockSchedulerProvider.mainScheduler()).thenReturn(Schedulers.trampoline())
        presenter = DetailsPresenter(
            mockArticlesRepository,
            mockSchedulerProvider
        )
    }

    @Test
    fun `given register called when getArticle is successful then call renderDetails`() {
        whenever(mockArticlesRepository.getArticle(MOCK_ARTICLE_URL)).thenReturn(Single.just(mockArticleDetails))

        presenter.register(MOCK_ARTICLE_URL, mockView)

        verify(mockView).renderDetails(mockArticleDetails)
    }

    @Test
    fun `given register called when getArticle fails then call showError`() {
        val mockError = Throwable()
        whenever(mockArticlesRepository.getArticle(MOCK_ARTICLE_URL)).thenReturn(Single.error(mockError))

        presenter.register(MOCK_ARTICLE_URL, mockView)

        val argumentCaptor = argumentCaptor<ArticleFetchError>()
        verify(mockView).showError(argumentCaptor.capture())
        assertEquals(mockError, argumentCaptor.firstValue.throwable)
    }
}