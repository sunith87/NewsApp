package com.newsapp

import android.app.Application
import com.newsapp.dagger.component.ArticlesComponent
import com.newsapp.dagger.component.DaggerArticlesComponent
import com.newsapp.dagger.module.AndroidModule
import com.newsapp.dagger.module.ArticleDataModule

class NewsApp : Application() {

    lateinit var appComponent: ArticlesComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = initDagger()
    }

    private fun initDagger(): ArticlesComponent =
        DaggerArticlesComponent.builder()
            .articleDataModule(ArticleDataModule())
            .androidModule(AndroidModule(this))
            .build()
}