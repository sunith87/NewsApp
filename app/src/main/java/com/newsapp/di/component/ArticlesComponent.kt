package com.newsapp.di.component

import com.newsapp.di.module.AndroidModule
import com.newsapp.di.module.ArticleDataModule
import com.newsapp.ui.articlelist.ArticleListActivity
import com.newsapp.ui.details.DetailsActivity
import dagger.Component

@Component(modules = [AndroidModule::class, ArticleDataModule::class])
interface ArticlesComponent {
    fun inject(articleListActivity: ArticleListActivity)
    fun inject(detailsActivity: DetailsActivity)
}