package com.chandan.newsapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.chandan.newsapp.R
import com.chandan.newsapp.db.ArticleDatabase
import com.chandan.newsapp.repository.NewsRepository
import com.chandan.newsapp.ui.viewmodel.NewsViewModel
import com.chandan.newsapp.ui.viewmodel.NewsViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel =ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)

        setContentView(R.layout.activity_news)
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }
}