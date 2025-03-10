package com.example.news_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
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

class SourceScreen : AppCompatActivity() {
    private lateinit var greetingText: TextView
    private lateinit var categorySpinner: Spinner
    private lateinit var sourcesRecyclerView: RecyclerView
    private lateinit var skipButton: Button
    private lateinit var newsManager: NewsManager

    private var selectedCategory = "general"  // Default category
    private lateinit var searchTerm: String
    private lateinit var apiKey: String
    private lateinit var sourcesAdapter: SourceAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_source_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Initialize Views
        categorySpinner = findViewById(R.id.spinnerSourcesCategories)
        sourcesRecyclerView = findViewById(R.id.source_recycler)
        skipButton = findViewById(R.id.skipbutton)
        newsManager = NewsManager()
        apiKey = getString(R.string.apinews)

        greetingText = findViewById(R.id.termsearchedtext)
        searchTerm = intent.getStringExtra("location") ?: ""
        var final = "Search for: $searchTerm"
        greetingText.text = final


        //Set up RecyclerView
        sourcesRecyclerView.layoutManager = LinearLayoutManager(this)
        sourcesAdapter = SourceAdapter(mutableListOf()) { selectedSource ->
            navigateToArticles(selectedSource.id, selectedSource.name)
        }
        sourcesRecyclerView.adapter = sourcesAdapter

        // Set up Spinner with supported categories
        ArrayAdapter.createFromResource(
            this,
            R.array.headline_category,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categorySpinner.adapter = adapter
        }
        // Handle category selection changes
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                selectedCategory = parent.getItemAtPosition(position).toString().lowercase()
                fetchSources()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }


        }
        skipButton.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        val articles = newsManager.retrieveArticlesByQuery(searchTerm, apiKey)

                        withContext(Dispatchers.Main) {
                            if (articles.isNotEmpty()) {
                                val intent = Intent(this@SourceScreen, ArticlesScreen::class.java).apply {
                                    putExtra("searchTerm", searchTerm)
                                }
                                startActivity(intent)
                            } else {
                                Toast.makeText(this@SourceScreen, "No articles found for '$searchTerm'", Toast.LENGTH_LONG).show()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@SourceScreen, "Error loading articles: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        fetchSources()

    }

    private fun fetchSources() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val sources = newsManager.retrieveSourcesByKeyword(selectedCategory, searchTerm, apiKey)
                    Log.d("FETCH_SOURCES", "Fetching sources for searchTerm: '$searchTerm' and category: '$selectedCategory'")
                    withContext(Dispatchers.Main) {
                        if (sources.isNotEmpty()) {
                            sourcesAdapter.updateSources(sources)
                        } else {
                            Toast.makeText(this@SourceScreen, "No sources found for '$searchTerm' in $selectedCategory", Toast.LENGTH_LONG).show()
                            sourcesAdapter.updateSources(emptyList())
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@SourceScreen, "Error loading sources: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


    private fun navigateToArticles(sourceId: String?, sourceName: String?) {
        val intent = Intent(this, ArticlesScreen::class.java).apply {
            putExtra("searchTerm", searchTerm)
            putExtra("sourceId", sourceId)  // Pass source ID
            putExtra("sourceName", sourceName)
        }
        startActivity(intent)
    }


}