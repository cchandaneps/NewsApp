package com.chandan.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chandan.newsapp.R
import com.chandan.newsapp.adapters.NewsAdapter
import com.chandan.newsapp.ui.NewsActivity
import com.chandan.newsapp.ui.viewmodel.NewsViewModel
import com.chandan.newsapp.util.Constants.Companion.DELAY_IN_SEARCH
import com.chandan.newsapp.util.Resource
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {
    val TAG = "NewsApp- SearchNewsFragment"
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel

        setUpRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle= Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        var job: Job?=null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job= MainScope().launch {
                delay(DELAY_IN_SEARCH)
                editable?.let {
                    if(editable.toString().isNotEmpty()){
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resource.Success->{
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error ->{
                    hideProgressBar()
                    response.message?.let { message->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading->{
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar(){
        paginationProgressBar.visibility=View.INVISIBLE
    }

    private fun showProgressBar(){
        paginationProgressBar.visibility=View.VISIBLE
    }

    private fun setUpRecyclerView(){
        newsAdapter = NewsAdapter()
        rvSearchNews.apply {
            adapter=newsAdapter
            layoutManager= LinearLayoutManager(activity)
        }
    }
}