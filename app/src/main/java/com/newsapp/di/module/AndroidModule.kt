package com.newsapp.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AndroidModule(private val app: Application) {

    @Provides
    fun providesContext(): Context {
        return app
    }
}