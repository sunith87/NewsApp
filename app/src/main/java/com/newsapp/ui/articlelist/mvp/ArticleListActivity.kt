package com.newsapp.ui.articlelist.mvp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.newsapp.NewsApp
import com.newsapp.R
import com.newsapp.ui.articlelist.ArticleClickListener
import com.newsapp.ui.articlelist.adapter.ArticlesAdapter
import com.newsapp.ui.articlelist.model.Article
import com.newsapp.ui.articlelist.model.ListItem
import com.newsapp.ui.articlelist.model.error.ArticleFetchError
import com.newsapp.ui.details.DetailsActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_article_list.*
import javax.inject.Inject

class ArticleListActivity : AppCompatActivity(), ArticleListView {

    @Inject
    lateinit var presenter: ArticleListPresenter
    @Inject
    lateinit var adapter: ArticlesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_list)
        setSupportActionBar(toolbar)
        (application as NewsApp).appComponent.inject(this)

        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter

        swipe_refreshlayout.setOnRefreshListener { presenter.onRefresh() }

        presenter.register(this)
    }

    override fun onDestroy() {
        presenter.unregister()
        super.onDestroy()
    }

    override fun showArticles(articles: List<ListItem>) {
        runOnUiThread {
            adapter.showArticles(articles)
        }
    }

    override fun onArticleClicked(): Observable<Article> {
        return Observable.create { emitter ->
            adapter.setArticleClickListener(object : ArticleClickListener {
                override fun onClicked(article: Article) {
                    emitter.onNext(article)
                }
            })
        }
    }

    override fun openArticleDetail(article: Article) {
        val intent = DetailsActivity.startActivityIntent(this, article)
        startActivity(intent)
    }

    override fun handlerError(error: ArticleFetchError) {
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

    override fun showRefreshing(isRefreshing: Boolean) {
        runOnUiThread {
            swipe_refreshlayout.isRefreshing = isRefreshing
        }
    }
}
