package com.chandan.newsapp.repository

import com.chandan.newsapp.api.RetrofitInstance
import com.chandan.newsapp.db.ArticleDatabase
import com.chandan.newsapp.models.Article

class NewsRepository(
    val db: ArticleDatabase
) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int)=
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun searchNews(queryString: String, pageNumber: Int)=
        RetrofitInstance.api.searchForNews(queryString,pageNumber)

    suspend fun upsert(article:Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun delete(article: Article) = db.getArticleDao().delete(article)
}