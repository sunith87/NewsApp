package com.newsapp.base

import com.nhaarman.mockitokotlin2.verify
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.junit.Test

import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks

class BasePresenterTest {

    @Mock
    private lateinit var mockDisposable: Disposable
    @Mock
    private lateinit var mockCompositeDisposable: CompositeDisposable
    @Mock
    private lateinit var mockBaseView: BaseView
    @Mock
    private lateinit var mockBaseView2: BaseView

    private lateinit var cut: BasePresenter<BaseView>

    @Before
    fun setup() {
        initMocks(this)
        cut = BasePresenter(mockCompositeDisposable)
    }

    @Test(expected = IllegalStateException::class)
    fun `given view already registered when another view registered an error thrown`() {
        cut.register(mockBaseView)
        cut.register(mockBaseView2)
    }

    @Test(expected = IllegalStateException::class)
    fun `given unregister called when view is null then an error thrown`() {
        cut.unregister()
    }

    @Test
    fun `given addDisposable called then added to Composite Disposable`() {
        cut.addDisposable(mockDisposable)

        verify(mockCompositeDisposable).add(mockDisposable)
    }

    @Test
    fun `given unregister called after register then disposables are cleared`() {
        cut.register(mockBaseView)
        cut.unregister()

        verify(mockCompositeDisposable).clear()
    }
}