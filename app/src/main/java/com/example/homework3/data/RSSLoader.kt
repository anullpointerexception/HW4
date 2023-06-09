package com.example.homework3.data

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
// "https://www.engadget.com/rss.xml"
class RSSLoader {
    fun fetchRSS(feedUrl: String): FetchRSSResult {
        val urlConnection = URL(feedUrl)
            .openConnection() as HttpURLConnection
        return try{
            urlConnection.run {
                requestMethod = "GET"
                connectTimeout = 5000
                readTimeout = 5000
                inputStream.bufferedReader().use {
                    it.readText()
                }.let { Success(it) }
            }
            } catch(ioException: IOException){
                Failed("There was an error fetch the xml", throwable = ioException)
            } finally {
                urlConnection.disconnect()
            }
        }
}
sealed interface FetchRSSResult
class Success(val result: String) : FetchRSSResult
class Failed(val text: String, val throwable: Throwable) : FetchRSSResult