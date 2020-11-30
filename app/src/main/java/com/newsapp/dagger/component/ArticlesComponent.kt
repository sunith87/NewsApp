package com.newsapp.dagger.component

import com.newsapp.dagger.module.AndroidModule
import com.newsapp.dagger.module.ArticleDataModule
import com.newsapp.ui.articlelist.mvp.ArticleListActivity
import com.newsapp.ui.articlelist.mvvm.ArticleListViewModelActivity
import com.newsapp.ui.details.DetailsActivity
import dagger.Component

@Component(modules = [AndroidModule::class, ArticleDataModule::class])
interface ArticlesComponent {
    fun inject(articleListActivity: ArticleListActivity)
    fun inject(articleListViewModelActivity: ArticleListViewModelActivity)
    fun inject(detailsActivity: DetailsActivity)
}