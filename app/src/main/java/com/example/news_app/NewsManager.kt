package com.example.news_app

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject

class NewsManager {
    private val okHttpClient: OkHttpClient

    init {
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)

        okHttpClient = builder.build()
    }

    suspend fun retrieveArticlesBySource(sourceId: String, searchTerm: String, apiKey: String): List<Article> {
        val url = "https://newsapi.org/v2/top-headlines?sources=$sourceId&q=$searchTerm&apiKey=$apiKey"

        val request = Request.Builder().url(url).build()
        val response = okHttpClient.newCall(request).execute()
        val responseBody = response.body?.string()

        if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
            val articles = mutableListOf<Article>()
            val json = JSONObject(responseBody)
            val articlesArray = json.getJSONArray("articles")

            for (i in 0 until articlesArray.length()) {
                val currentArticle = articlesArray.getJSONObject(i)
                val sourceObject = currentArticle.getJSONObject("source")

                val article = Article(
                    source = sourceObject.getString("name"),
                    author = currentArticle.optString("author", "Unknown Author"),
                    title = currentArticle.getString("title"),
                    description = currentArticle.optString("description", "No description available"),
                    url = currentArticle.getString("url"),
                    urlToImage = currentArticle.optString("urlToImage", ""),
                    publishedAt = currentArticle.getString("publishedAt"),
                    content = currentArticle.optString("content", "No content available")
                )
                articles.add(article)
            }
            return articles
        } else {
            return emptyList()
        }
    }
    suspend fun retrieveNewsByCountry(countryCode: String, apiKey: String): List<Article> {
        val url = "https://newsapi.org/v2/top-headlines?country=$countryCode&apiKey=$apiKey"

        val request = Request.Builder().url(url).build()
        val response = okHttpClient.newCall(request).execute()
        val responseBody = response.body?.string()

        if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
            val articles = mutableListOf<Article>()
            val json = JSONObject(responseBody)
            val articlesArray = json.getJSONArray("articles")

            for (i in 0 until articlesArray.length()) {
                val currentArticle = articlesArray.getJSONObject(i)
                val sourceObject = currentArticle.getJSONObject("source")

                val article = Article(
                    source = sourceObject.getString("name"),
                    author = currentArticle.optString("author", "Unknown Author"),
                    title = currentArticle.getString("title"),
                    description = currentArticle.optString("description", "No description available"),
                    url = currentArticle.getString("url"),
                    urlToImage = currentArticle.optString("urlToImage", ""),
                    publishedAt = currentArticle.getString("publishedAt"),
                    content = currentArticle.optString("content", "No content available")
                )
                articles.add(article)
            }
            return articles
        } else {
            return emptyList()
        }
    }

    //retieves for the topheadlines
    suspend fun retrieveNews(category: String, page: Int, apiKey: String, country: String): Pair<List<Article>, Int> {
        val request=Request.Builder()
            .url("https://newsapi.org/v2/top-headlines?country=$country&category=$category&page=$page&apiKey=$apiKey")
            .build()

        val response = okHttpClient.newCall(request).execute()
        val responseBody = response.body?.string()

        if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
            val articles = mutableListOf<Article>()
            val json = JSONObject(responseBody)
            val totalResults = json.getInt("totalResults")
            val articlesArray = json.getJSONArray("articles")

            for (i in 0 until articlesArray.length()) {
                val currentArticle = articlesArray.getJSONObject(i)
                val sourceObject = currentArticle.getJSONObject("source")

                val article = Article(
                    source = sourceObject.getString("name"),
                    author = currentArticle.optString("author", "Unknown Author"),
                    title = currentArticle.getString("title"),
                    description = currentArticle.optString("description", "No description available"),
                    url = currentArticle.getString("url"),
                    urlToImage = currentArticle.optString("urlToImage", ""),
                    publishedAt = currentArticle.getString("publishedAt"),
                    content = currentArticle.optString("content", "No content available")
                )
                articles.add(article)
            }
            return Pair(articles, totalResults)
        } else {
            return Pair(listOf(), 0)
        }
    }

    suspend fun retrieveNewsByQuery(searchTerm: String, apiKey: String): List<Article> {
        val url = "https://newsapi.org/v2/top-headlines?q=$searchTerm&apiKey=$apiKey"

        val request = Request.Builder().url(url).build()
        val response = okHttpClient.newCall(request).execute()
        val responseBody = response.body?.string()

        if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
            val articles = mutableListOf<Article>()
            val json = JSONObject(responseBody)
            val articlesArray = json.getJSONArray("articles")

            for (i in 0 until articlesArray.length()) {
                val currentArticle = articlesArray.getJSONObject(i)
                val sourceObject = currentArticle.getJSONObject("source")

                val article = Article(
                    source = sourceObject.getString("name"),
                    author = currentArticle.optString("author", "Unknown Author"),
                    title = currentArticle.getString("title"),
                    description = currentArticle.optString("description", "No description available"),
                    url = currentArticle.getString("url"),
                    urlToImage = currentArticle.optString("urlToImage", ""),
                    publishedAt = currentArticle.getString("publishedAt"),
                    content = currentArticle.optString("content", "No content available")
                )
                articles.add(article)
            }
            return articles
        } else {
            return emptyList()
        }
    }

    suspend fun retrieveArticlesByQuery(searchTerm: String, apiKey: String): List<Article> {
        val url = "https://newsapi.org/v2/top-headlines?q=$searchTerm&apiKey=$apiKey"  // ✅ Use correct query

        val request = Request.Builder().url(url).build()
        val response = okHttpClient.newCall(request).execute()
        val responseBody = response.body?.string()

        if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
            val articles = mutableListOf<Article>()
            val json = JSONObject(responseBody)
            val articlesArray = json.getJSONArray("articles")

            for (i in 0 until articlesArray.length()) {
                val currentArticle = articlesArray.getJSONObject(i)
                val sourceObject = currentArticle.getJSONObject("source")

                val article = Article(
                    source = sourceObject.getString("name"),
                    author = currentArticle.optString("author", "Unknown Author"),
                    title = currentArticle.getString("title"),
                    description = currentArticle.optString("description", "No description available"),
                    url = currentArticle.getString("url"),
                    urlToImage = currentArticle.optString("urlToImage", ""),
                    publishedAt = currentArticle.getString("publishedAt"),
                    content = currentArticle.optString("content", "No content available")
                )
                articles.add(article)
            }
            return articles
        } else {
            return emptyList()
        }
    }


    //working
//    suspend fun retrieveArticlesByQuery(searchTerm: String, apiKey: String): List<Article> {
//        val url = "https://newsapi.org/v2/top-headlines?q=$searchTerm&apiKey=$apiKey"
//
//        val request = Request.Builder().url(url).build()
//        val response = okHttpClient.newCall(request).execute()
//        val responseBody = response.body?.string()
//
//        if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
//            val articles = mutableListOf<Article>()
//            val json = JSONObject(responseBody)
//            val articlesArray = json.getJSONArray("articles")
//
//            for (i in 0 until articlesArray.length()) {
//                val currentArticle = articlesArray.getJSONObject(i)
//                val sourceObject = currentArticle.getJSONObject("source")
//
//                val article = Article(
//                    source = sourceObject.getString("name"),
//                    author = currentArticle.optString("author", "Unknown Author"),
//                    title = currentArticle.getString("title"),
//                    description = currentArticle.optString("description", "No description available"),
//                    url = currentArticle.getString("url"),
//                    urlToImage = currentArticle.optString("urlToImage", ""),
//                    publishedAt = currentArticle.getString("publishedAt"),
//                    content = currentArticle.optString("content", "No content available")
//                )
//                articles.add(article)
//            }
//            return articles
//        } else {
//            return emptyList()
//        }
//    }


    suspend fun retrieveSourcesByKeyword(category: String?, searchTerm: String, apiKey: String): List<Source> {
        val baseUrl = "https://newsapi.org/v2/sources"

        val urlBuilder = StringBuilder(baseUrl)
        urlBuilder.append("?apiKey=$apiKey")

        if (searchTerm.isNotEmpty()) {
            urlBuilder.append("&q=${searchTerm}")  // ✅ Filter by search query
        }

        if (!category.isNullOrEmpty()) {
            urlBuilder.append("&category=${category}")  // ✅ Filter by category
        }

        val finalUrl = urlBuilder.toString()
        Log.d("API_REQUEST", "Final URL used to fetch sources: $finalUrl")  // ✅ Debugging Log

        val request = Request.Builder().url(urlBuilder.toString()).build()
        val response = okHttpClient.newCall(request).execute()
        val responseBody = response.body?.string()

        if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
            val sources = mutableListOf<Source>()
            val json = JSONObject(responseBody)
            val sourcesArray = json.getJSONArray("sources")

            for (i in 0 until sourcesArray.length()) {
                val currentSource = sourcesArray.getJSONObject(i)
                val source = Source(
                    id = currentSource.getString("id"),
                    name = currentSource.getString("name"),
                    description = currentSource.optString("description", "No description available"),
                )
                sources.add(source)
            }

            return sources
        } else {
            return emptyList()
        }
    }


////working
//    suspend fun retrieveSourcesByKeyword(category: String?, searchTerm: String?, apiKey: String): List<Source> {
//        val baseUrl = "https://newsapi.org/v2/top-headlines/sources"
//
//        val urlBuilder = StringBuilder(baseUrl)
//        urlBuilder.append("?apiKey=$apiKey")
//
//        if (!searchTerm.isNullOrEmpty()) {
//            urlBuilder.append("&q=${searchTerm}")
//        }
//
//        if (!category.isNullOrEmpty()) {
//            urlBuilder.append("&category=${category}")
//        }
//
//        // ⚠️ No country filter here, since we're dealing with sources
//        val request = Request.Builder().url(urlBuilder.toString()).build()
//
//        val response = okHttpClient.newCall(request).execute()
//        val responseBody = response.body?.string()
//
//        if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
//            val sources = mutableListOf<Source>()
//            val json = JSONObject(responseBody)
//            val sourcesArray = json.getJSONArray("sources")
//
//            for (i in 0 until sourcesArray.length()) {
//                val currentSource = sourcesArray.getJSONObject(i)
//                val source = Source(
//                    name = currentSource.getString("name"),
//                    description = currentSource.optString("description", "No description available"),
//                    id = currentSource.getString("id"),
//                )
//                sources.add(source)
//            }
//            return sources
//        } else {
//            return emptyList()
//        }
//    }

}