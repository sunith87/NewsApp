package com.newsapp.ui.articlelist.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newsapp.ui.articlelist.model.Data
import com.newsapp.ui.articlelist.model.Failure
import com.newsapp.ui.articlelist.model.Success
import com.newsapp.ui.data.ArticlesRepository
import kotlinx.coroutines.*

class ArticleListViewModel(
    private val articlesRepository: ArticlesRepository
) : ViewModel() {

    private var refreshJob: Job? = null
    private var liveData: MutableLiveData<Data>? = null

    fun getArticleList(): LiveData<Data>? {
        if (liveData == null) {
            liveData = MutableLiveData()
            onRefresh()
        }
        return liveData
    }

    fun onRefresh() {
        refreshJob = viewModelScope.async {
            try {
                val latestArticles = fetchData()
                liveData?.postValue(Success(latestArticles))
            } catch (error: Throwable) {
                liveData?.postValue(Failure(error))
            }
        }
    }

    private suspend fun fetchData() = articlesRepository.latestArticlesAsync()
}


