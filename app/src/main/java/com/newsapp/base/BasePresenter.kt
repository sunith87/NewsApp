package com.newsapp.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BasePresenter<V : BaseView>(private val disposables: CompositeDisposable) {

    var view: V? = null
        private set

    open fun register(view: V) {
        this.view?.also {
            throw IllegalStateException("View $it is already attached. Cannot attach another $view")
        }
        this.view = view
    }

    open fun unregister() {
        if (view != null) {
            view = null
            disposables.clear()
        } else {
            throw IllegalStateException("View is already detached")
        }
    }

    fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }
}