package com.example.news_app

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

object NewsApiService {
    private const val API_KEY = "eda13923ef944500aabff54351c24e7d"
    private const val BASE_URL = "https://newsapi.org/v2/top-headlines"

    private val client = OkHttpClient()

    suspend fun fetchNewsByCountry(country: String): List<Article> {

        val st = country.lowercase()
        val url = "$BASE_URL?country=$st&apiKey=$API_KEY"

        Log.d("API_REQUEST", "Requesting: $url")

        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
            Log.d("API_RESPONSE", "Response: $responseBody")

            val articles = mutableListOf<Article>()
            val json = JSONObject(responseBody)
            val articlesArray = json.optJSONArray("articles") ?: return emptyList()

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

            Log.d("API_RESPONSE", "Articles found: ${articles.size}")
            return articles
        } else {
            Log.e("API_ERROR", "Failed response: ${responseBody ?: "No response"}")
            return emptyList()
        }
    }
}
