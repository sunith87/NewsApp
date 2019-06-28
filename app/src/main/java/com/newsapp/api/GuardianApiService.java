package com.newsapp.api;

import com.newsapp.api.model.RawArticleListResponse;
import com.newsapp.api.model.RawArticleDetailsResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface GuardianApiService {

    @GET("search?show-fields=headline,thumbnail&page-size=50")
    Single<RawArticleListResponse> searchArticles(@Query("q") String searchTerm);

    @GET
    Single<RawArticleDetailsResponse> getArticle(@Url String articleUrl, @Query("show-fields") String fields);
}
