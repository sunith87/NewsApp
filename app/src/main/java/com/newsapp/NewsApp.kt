package com.newsapp

import android.app.Application
import com.newsapp.di.component.ArticlesComponent
import com.newsapp.di.component.DaggerArticlesComponent
import com.newsapp.di.module.AndroidModule
import com.newsapp.di.module.ArticleDataModule

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