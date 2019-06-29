package com.newsapp.ui.details

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.widget.Toast

import com.bumptech.glide.Glide
import com.newsapp.ui.articlelist.model.ArticleDetails
import com.newsapp.ui.articlelist.model.error.ArticleFetchError

import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast.LENGTH_LONG
import com.newsapp.NewsApp
import com.newsapp.R
import com.newsapp.ui.articlelist.model.Article
import kotlinx.android.synthetic.main.activity_article_details.*
import javax.inject.Inject

class DetailsActivity : Activity(), DetailsPresenterView {

    companion object {
        private const val DATA_ARTICLE_URL_KEY = "DATA_ARTICLE_URL_KEY"

        fun startActivityIntent(context: Context, article: Article): Intent {
            return Intent(context, DetailsActivity::class.java).also {
                it.putExtra(DATA_ARTICLE_URL_KEY, article.url)
            }
        }
    }

    @Inject
    lateinit var presenter: DetailsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_details)
        val url = intent.getStringExtra(DATA_ARTICLE_URL_KEY)
        details_fetch_progress.visibility = VISIBLE
        (application as NewsApp).appComponent.inject(this)
        presenter.register(url, this)
    }

    override fun onDestroy() {
        presenter.unregister()
        super.onDestroy()
    }

    override fun renderDetails(articleDetails: ArticleDetails) {
        details_fetch_progress.visibility = GONE
        article_headline_textview.text = articleDetails.headline
        setBody(articleDetails)
        Glide.with(this).load(articleDetails.thumbnail).into(details_image)
    }

    private fun setBody(articleDetails: ArticleDetails) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            article_details_textview.text = Html.fromHtml(articleDetails.body, Html.FROM_HTML_MODE_LEGACY)
        } else {
            article_details_textview.text = Html.fromHtml(articleDetails.body)
        }
    }

    override fun showError(articleDetailsError: ArticleFetchError) {
        details_fetch_progress.visibility = GONE
        Toast.makeText(this, "Cannot fetch details, " + articleDetailsError.throwable.localizedMessage, LENGTH_LONG)
            .show()
    }
}
