package com.newsapp.api;

import com.newsapp.api.model.RawArticleListResponse;
import com.newsapp.api.model.RawArticleDetailsResponse;

import kotlinx.coroutines.Deferred;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface GuardianApiService {

    @GET("search?show-fields=headline,thumbnail&page-size=50")
    Deferred<RawArticleListResponse> searchArticles(@Query("q") String searchTerm);

    @GET
    Deferred<RawArticleDetailsResponse> getArticle(@Url String articleUrl, @Query("show-fields") String fields);
}
