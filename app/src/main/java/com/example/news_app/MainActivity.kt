package com.example.news_app

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var searchtext: EditText
    private lateinit var searchbutton: Button
    private lateinit var localnewsButton: Button
    private lateinit var topheadlinesbutton: Button
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        searchtext = findViewById(R.id.SearchInputText)
        searchbutton = findViewById(R.id.SearchButton)
        localnewsButton = findViewById(R.id.LocalNewsButton)
        topheadlinesbutton = findViewById(R.id.TopHeadlinesButton)

        sharedPrefs = getSharedPreferences("news_prefs", MODE_PRIVATE)

        searchtext.addTextChangedListener(myTextWatcher)

        // Restore the search term if one was previously saved
        val savedSearchTerm = sharedPrefs.getString("SEARCH_TERM", "")
        searchtext.setText(savedSearchTerm)

        searchbutton.setOnClickListener {
            val inputsearch:String=searchtext.text.toString().trim()
            sharedPrefs
                .edit()
                .putString("SEARCH_TERM", inputsearch)
                .apply()


            val Sourcescreen1 = Intent(this@MainActivity, SourceScreen::class.java)
            var nameofuser = searchtext.text
            Sourcescreen1.putExtra("location", "$nameofuser")
            startActivity(Sourcescreen1)

        }

        topheadlinesbutton.setOnClickListener {

            val Sourcescreen1 = Intent(this@MainActivity, TopHeadlines::class.java)
            var nameofuser = searchtext.text
            Sourcescreen1.putExtra("location", "$nameofuser")
            startActivity(Sourcescreen1)

        }

        localnewsButton.setOnClickListener {

            val Sourcescreen1 = Intent(this@MainActivity, MapsGoogleActivity::class.java)
            var nameofuser = searchtext.text
            Sourcescreen1.putExtra("location", "$nameofuser")
            startActivity(Sourcescreen1)

        }



    }



    private val myTextWatcher: TextWatcher =object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            var inputtedsearchtext = searchtext.text.toString()

            val enablebutton : Boolean = inputtedsearchtext.isNotBlank()
            searchbutton.setEnabled(enablebutton)
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }
}