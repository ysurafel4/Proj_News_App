package com.example.news_app

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TopHeadlines : AppCompatActivity() {

    private lateinit var headlinesRecyclerView: RecyclerView
    private lateinit var categorySpinner: Spinner
    private lateinit var prevPageButton: Button
    private lateinit var nextPageButton: Button
    private lateinit var pageIndicator: TextView
    private lateinit var newsManager: NewsManager
    private lateinit var sharedPreferences: SharedPreferences

    private var currentPage = 1
    private var currentCategory = "general"
    private lateinit var apiKey: String
    private val country = "us"
    private var totalResults = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_top_headlines)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Views

        headlinesRecyclerView = findViewById(R.id.recyclerViewTopHeadlines)
        categorySpinner = findViewById(R.id.headlinesSpinner)
        prevPageButton = findViewById(R.id.prevPageButton)
        nextPageButton = findViewById(R.id.nextPageButton)
        pageIndicator = findViewById(R.id.pageIndicator)
        newsManager = NewsManager()
        apiKey = getString(R.string.apinews) // Fetch API key from resources
        // Set up RecyclerView with an empty adapter initially
        headlinesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@TopHeadlines)
            adapter = TopHeadlinesAdapter(emptyList())
        }
        sharedPreferences =getSharedPreferences("NewsPrefs", Context.MODE_PRIVATE)

        // Restore last category selection
        currentCategory = sharedPreferences.getString("selectedCategory", "general") ?: "general"

        // Set up RecyclerView
        headlinesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@TopHeadlines)
            adapter = TopHeadlinesAdapter(emptyList())
        }


        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter.createFromResource(
            this,
            R.array.headline_category,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            categorySpinner.adapter = adapter
        }

        // Set last selected category
        val position = (categorySpinner.adapter as ArrayAdapter<String>).getPosition(currentCategory)
        categorySpinner.setSelection(position)

        // Set up Spinner listener
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                currentCategory = parent.getItemAtPosition(position).toString().lowercase()
                sharedPreferences.edit().putString("selectedCategory", currentCategory).apply()
                currentPage = 1
                fetchHeadlines()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Handle Paging
        prevPageButton.setOnClickListener {
            if (currentPage > 1) {
                currentPage--
                fetchHeadlines()
            }
        }
        nextPageButton.setOnClickListener {
            if ((currentPage * 20) < totalResults) {
                currentPage++
                fetchHeadlines()
            }
        }

        // Load initial headlines
        fetchHeadlines()
    }

    private fun fetchHeadlines() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val (articles, total) = newsManager.retrieveNews(currentCategory, currentPage, apiKey, country)
                    totalResults = total
                    withContext(Dispatchers.Main) {
                        if (articles.isNotEmpty()) {
                            headlinesRecyclerView.adapter = TopHeadlinesAdapter(articles)
                            pageIndicator.text = "Page $currentPage of ${((totalResults + 19) / 20)}"
                            prevPageButton.isEnabled = currentPage > 1
                            nextPageButton.isEnabled = (currentPage * 20) < totalResults
                        } else {
                            Toast.makeText(this@TopHeadlines, "No news found.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@TopHeadlines, "Error loading news: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}