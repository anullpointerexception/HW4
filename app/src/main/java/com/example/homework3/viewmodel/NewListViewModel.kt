package com.example.homework3.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework3.MainActivity
import com.example.homework3.data.*
import kotlinx.coroutines.*
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class NewListViewModel : ViewModel() {

    private val rssLoader = RSSLoader()

    private val rssParser = RSSParser()

    private val _rssList = MutableLiveData(emptyList<Item>())
    //Use LiveData to update the UI automatically after the data is downloaded and parsed.
    val rssList: LiveData<List<Item>> = _rssList

    private val _hasError = MutableLiveData(false)
    val hasError: LiveData<Boolean> = _hasError

    private var feedUrl: String = "https://www.engadget.com/rss.xml"
    // https://www.feedforall.com/sample.xml

    fun loadData(){
        viewModelScope.launch {
            var rssResultList = fetchRSSResult(feedUrl)
                ?: return@launch

            if(rssResultList.isEmpty()){
                rssResultList = fetchRSSResult(feedUrl)
                    ?: return@launch
            }
            _rssList.postValue(rssResultList)
        }
    }

    private suspend fun fetchRSSResult(url: String): List<Item>? {
        val res = withContext(Dispatchers.IO){
            rssLoader.fetchRSS(url)
        }
        return when(res){
            is Success -> {
                val rssDataList = withContext(Dispatchers.Default){
                    rssParser.parseResult(res.result)
                }
                rssDataList?.sortedBy { it.pubDate }
            }
            is Failed -> {
                Log.e(
                    MainActivity.LOG_TAG,
                    "Exception occured",
                    res.throwable
                )
                _hasError.postValue(true)
                null
            }
        }
    }

    fun updateFeedUrl(newUrl: String){
        feedUrl = newUrl
        loadData()
    }

}