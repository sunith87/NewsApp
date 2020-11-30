package com.newsapp.ui.articlelist.model

sealed class Data
class Success(val articles: List<ListItem>) :Data()
class Failure(val throwable: Throwable) :Data()
