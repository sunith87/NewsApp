package com.newsapp.ui.articlelist.mvvm

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.newsapp.NewsApp
import com.newsapp.R
import com.newsapp.ui.articlelist.ArticleClickListener
import com.newsapp.ui.articlelist.adapter.ArticlesAdapter
import com.newsapp.ui.articlelist.model.*
import com.newsapp.ui.articlelist.model.error.ArticleFetchError
import com.newsapp.ui.details.DetailsActivity
import kotlinx.android.synthetic.main.activity_article_list.*
import javax.inject.Inject

class ArticleListViewModelActivity : AppCompatActivity() {

    private val listViewModel: ArticleListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ArticleListViewModel::class.java)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var adapter: ArticlesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_list)
        setSupportActionBar(toolbar)
        (application as NewsApp).appComponent.inject(this)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter

        swipe_refreshlayout.setOnRefreshListener {
            showRefreshing(true)
            listViewModel.onRefresh()
        }
        adapter.setArticleClickListener(object : ArticleClickListener {
            override fun onClicked(article: Article) {
                openArticleDetail(article)
            }
        })
        listViewModel.getArticleList()?.observe(this, object : Observer<Data> {
            override fun onChanged(data: Data?) {
                showRefreshing(false)
                when (data) {
                    is Success -> {
                        showArticles(data.articles)
                    }
                    is Failure -> {
                        handlerError(ArticleFetchError(data.throwable))
                    }
                    null -> handlerError(ArticleFetchError(Throwable("null response")))
                }
            }

        })
    }

    private fun showArticles(articles: List<ListItem>) {
        runOnUiThread {
            adapter.showArticles(articles)
        }
    }

    private fun openArticleDetail(article: Article) {
        val intent = DetailsActivity.startActivityIntent(this, article)
        startActivity(intent)
    }

    private fun handlerError(error: ArticleFetchError) {
        runOnUiThread {
            showSnackbar("Article Fetch Error: Something went wrong, " + error.throwable.message)
        }
    }

    private fun showSnackbar(message: String) {
        runOnUiThread {
            val rootView = findViewById<View>(android.R.id.content)
            Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun showRefreshing(isRefreshing: Boolean) {
        runOnUiThread {
            swipe_refreshlayout.isRefreshing = isRefreshing
        }
    }
}