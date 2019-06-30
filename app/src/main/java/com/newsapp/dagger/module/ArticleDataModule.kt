package com.newsapp.dagger.module

import android.content.Context
import android.content.res.Resources
import com.google.gson.Gson
import com.newsapp.R
import com.newsapp.api.GuardianApiService
import com.newsapp.base.SchedulerProvider
import com.newsapp.ui.articlelist.ArticleListPresenter
import com.newsapp.ui.articlelist.adapter.ArticlesAdapter
import com.newsapp.ui.data.ArticleDetailsMapper
import com.newsapp.ui.data.ArticleMapper
import com.newsapp.ui.data.ArticlesRepository
import com.newsapp.ui.data.GuardianArticlesRepository
import com.newsapp.ui.details.DetailsPresenter
import com.newsapp.util.DateFormatUtil
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ArticleDataModule() {

    companion object {
        private const val BASE_URL = "https://content.guardianapis.com"
        private const val HEADER_API_KEY = "api-key"
    }

    @Provides
    fun provideArticlesRepository(
        apiService: GuardianApiService,
        articleMapper: ArticleMapper,
        articleDetailsMapper: ArticleDetailsMapper
    ): ArticlesRepository {
        return GuardianArticlesRepository(apiService, articleMapper, articleDetailsMapper)
    }

    @Provides
    fun provideDateFormatUtil(): DateFormatUtil {
        return DateFormatUtil()
    }

    @Provides
    fun provideArticleMapper(dateFormatUtil: DateFormatUtil): ArticleMapper {
        return ArticleMapper(dateFormatUtil)
    }

    @Provides
    fun provideArticleDetailsMapper(dateFormatUtil: DateFormatUtil): ArticleDetailsMapper {
        return ArticleDetailsMapper(dateFormatUtil)
    }

    @Provides
    fun provideArticlesAdapter(): ArticlesAdapter {
        return ArticlesAdapter()
    }

    @Provides
    fun provideArticleListPresenter(
        articlesRepository: ArticlesRepository,
        schedulerProvider: SchedulerProvider
    ): ArticleListPresenter {
        return ArticleListPresenter(articlesRepository, schedulerProvider, CompositeDisposable())
    }

    @Provides
    fun provideDetailsPresenter(
        articlesRepository: ArticlesRepository,
        schedulerProvider: SchedulerProvider
    ): DetailsPresenter {
        return DetailsPresenter(articlesRepository, schedulerProvider, CompositeDisposable())
    }

    @Provides
    fun provideGuardianService(context: Context): GuardianApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(createOkHttpClient(context.resources))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(GuardianApiService::class.java)
    }

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider {
        return object : SchedulerProvider {
            override fun ioScheduler() = Schedulers.io()
            override fun mainScheduler() = AndroidSchedulers.mainThread()
        }
    }

    private fun createOkHttpClient(resources: Resources): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.addInterceptor(getAuthInterceptor(resources))
        clientBuilder.addInterceptor(loggingInterceptor)
        return clientBuilder.build()
    }

    private fun getAuthInterceptor(resources: Resources): Interceptor {
        return Interceptor {
            val original = it.request()
            val hb = original.headers().newBuilder()
            hb.add(HEADER_API_KEY, resources.getString(R.string.guardian_api_key))
            it.proceed(original.newBuilder().headers(hb.build()).build())
        }
    }
}