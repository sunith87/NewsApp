package com.newsapp.ui.data

import com.newsapp.api.GuardianApiService
import com.newsapp.api.model.RawArticleDetailsResponse
import com.newsapp.api.model.RawArticleListResponse
import com.newsapp.ui.articlelist.model.ArticleDetails
import com.newsapp.ui.articlelist.model.ListItem
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks

class GuardianArticlesRepositoryTest {

    companion object {
        private const val MOCK_URL = "mock_url"
        private const val MOCK_FIELDS = "mock_fields"
    }

    private lateinit var cut: GuardianArticlesRepository

    @Mock
    private lateinit var mockListItem: ListItem
    @Mock
    private lateinit var mockListItem2: ListItem
    @Mock
    private lateinit var mockArticleDetails: ArticleDetails
    @Mock
    private lateinit var mockRawDetailsResponse: RawArticleDetailsResponse
    @Mock
    private lateinit var mockRawArticleListResponse: RawArticleListResponse
    @Mock
    private lateinit var mockGuardianApiService: GuardianApiService
    @Mock
    private lateinit var mockArticleMapper: ArticleMapper
    @Mock
    private lateinit var mockArticleDetailsMapper: ArticleDetailsMapper

    private val mockList = mutableListOf<ListItem>()
    @Before
    fun setUp() {
        initMocks(this)

        whenever(mockGuardianApiService.searchArticles(any())).thenReturn(Single.just(mockRawArticleListResponse))
        whenever(mockGuardianApiService.getArticle(any(), any())).thenReturn(Single.just(mockRawDetailsResponse))
        whenever(mockArticleMapper.map(mockRawArticleListResponse)).thenReturn(mockList)
        whenever(mockArticleDetailsMapper.mapToDetails(mockRawDetailsResponse)).thenReturn(mockArticleDetails)
        mockList.add(mockListItem)
        mockList.add(mockListItem2)
        cut = GuardianArticlesRepository(
            mockGuardianApiService,
            mockArticleMapper, mockArticleDetailsMapper
        )
    }

    @Test
    fun `given latestArticles called then search api called on guardian api service`() {
        cut.latestArticles().test()

        verify(mockGuardianApiService).searchArticles(ArticlesRepository.DEFAULT_SEARCH_TERM)
    }

    @Test
    fun `given latestArticles called then article mapper called`() {
        cut.latestArticles().test()

        verify(mockArticleMapper).map(mockRawArticleListResponse)
    }

    @Test
    fun `given latestArticles called when subscribed then a list of items returned`() {
        val test = cut.latestArticles().test()

        test.assertValue(mockList)
        assertTrue(mockList.contains(mockListItem))
        assertTrue(mockList.contains(mockListItem2))
    }

    @Test
    fun `given latestArticles called when subscribed and error thrown then error returned`() {
        val mockError = Throwable()
        whenever(mockGuardianApiService.searchArticles(any())).thenReturn(Single.error(mockError))

        val test = cut.latestArticles().test()

        test.assertError(mockError)
    }

    @Test
    fun `given getArticle called then get article api called on guardian api service`() {
        cut.getArticle(MOCK_URL, MOCK_FIELDS).test()

        verify(mockGuardianApiService).getArticle(MOCK_URL, MOCK_FIELDS)
    }

    @Test
    fun `given getArticle called then articleDetails mapper called`() {
        cut.getArticle(MOCK_URL, MOCK_FIELDS).test()

        verify(mockArticleDetailsMapper).mapToDetails(mockRawDetailsResponse)
    }

    @Test
    fun `given getArticle called when subscribed then a article details returned`() {
        val test = cut.getArticle(MOCK_URL, MOCK_FIELDS).test()

        test.assertValue(mockArticleDetails)
    }

    @Test
    fun `given getArticle called when subscribed and error thrown then error returned`() {
        val mockError = Throwable()
        whenever(mockGuardianApiService.getArticle(MOCK_URL, MOCK_FIELDS)).thenReturn(Single.error(mockError))

        val test = cut.getArticle(MOCK_URL, MOCK_FIELDS).test()

        test.assertError(mockError)
    }
}