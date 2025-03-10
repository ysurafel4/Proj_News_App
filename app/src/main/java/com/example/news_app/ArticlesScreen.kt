package com.example.news_app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticlesScreen : AppCompatActivity() {
    private lateinit var articlesAdapter: ArticleAdapter
    private lateinit var newsManager: NewsManager
    private lateinit var searchTerm: String
    private var sourceId: String? = null
    private var sourceName: String? = null
    private lateinit var APINEWS: String


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_articles_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setContentView(R.layout.activity_articles_screen)

        // Get intent data
        searchTerm = intent.getStringExtra("searchTerm") ?: ""
        sourceId = intent.getStringExtra("sourceId")
        sourceName = intent.getStringExtra("sourceName")
        APINEWS = getString(R.string.apinews)
        // Set up RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.artRecycle)
        recyclerView.layoutManager = LinearLayoutManager(this)

        articlesAdapter = ArticleAdapter(mutableListOf()) // Empty list initially
        recyclerView.adapter = articlesAdapter

        newsManager = NewsManager()

        fetchArticles()
    }

    private fun fetchArticles() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
//                    val articles = if (sourceId != null) {
                        val articles = if (!sourceId.isNullOrEmpty()) {
                        newsManager.retrieveArticlesBySource(sourceId!!, searchTerm, getString(R.string.apinews))
                    } else {
                        //newsManager.retrieveNews("", 1, getString(R.string.apinews), "us").first
                        newsManager.retrieveNewsByQuery(searchTerm, getString(R.string.apinews))
                    }

                    withContext(Dispatchers.Main) {
                        articlesAdapter.updateArticles(articles)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ArticlesScreen, "Error loading articles: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

}

