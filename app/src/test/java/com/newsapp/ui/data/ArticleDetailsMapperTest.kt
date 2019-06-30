package com.newsapp.ui.data

import com.newsapp.api.model.RawArticle
import com.newsapp.api.model.RawArticleDetailsResponse
import com.newsapp.api.model.RawArticleFields
import com.newsapp.api.model.RawDetailContent
import com.newsapp.util.DateFormatUtil
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import java.util.*

class ArticleDetailsMapperTest {

    companion object {
        private const val MOCK_DATE_STRING = "MOCK_DATE_STRING"
        private const val MOCK_MAIN_CONTENT = "mock_main_content"
        private const val MOCK_BODY = "mock_body"
        private const val MOCK_HEADLINE = "mock_headline"
        private const val MOCK_THUMBNAIL = "thumbnail"
        private const val MOCK_SECTION_NAME = "sectionName"
    }

    @Mock
    private lateinit var mockDate: Date
    @Mock
    private lateinit var mockFields: RawArticleFields
    @Mock
    private lateinit var mockRawArticle: RawArticle
    @Mock
    private lateinit var mockDetailContent: RawDetailContent
    @Mock
    private lateinit var mockDateFormatUtil: DateFormatUtil
    @Mock
    private lateinit var mockRawArticleDetailsResponse: RawArticleDetailsResponse

    private lateinit var cut: ArticleDetailsMapper

    @Before
    fun setUp() {
        initMocks(this)
        whenever(mockRawArticleDetailsResponse.response).thenReturn(mockDetailContent)
        whenever(mockDetailContent.content).thenReturn(mockRawArticle)
        whenever(mockRawArticle.fields).thenReturn(mockFields)
        whenever(mockRawArticle.webPublicationDate).thenReturn(mockDate)
        whenever(mockDateFormatUtil.getFormattedDate(mockDate)).thenReturn(MOCK_DATE_STRING)
        whenever(mockFields.main).thenReturn(MOCK_MAIN_CONTENT)
        whenever(mockFields.body).thenReturn(MOCK_BODY)
        whenever(mockFields.headline).thenReturn(MOCK_HEADLINE)
        whenever(mockFields.thumbnail).thenReturn(MOCK_THUMBNAIL)
        whenever(mockRawArticle.sectionName).thenReturn(MOCK_SECTION_NAME)
        cut = ArticleDetailsMapper(mockDateFormatUtil)
    }

    @Test
    fun `given dateformatter and response when mapToDetails called then correct article details returned`() {
        val articleDetails = cut.mapToDetails(mockRawArticleDetailsResponse)

        assertEquals(MOCK_MAIN_CONTENT, articleDetails.main)
        assertEquals(MOCK_BODY, articleDetails.body)
        assertEquals(MOCK_HEADLINE, articleDetails.headline)
        assertEquals(MOCK_THUMBNAIL, articleDetails.thumbnail)
        assertEquals(MOCK_SECTION_NAME, articleDetails.sectionName)
        assertEquals(MOCK_DATE_STRING, articleDetails.dateString)
    }
}