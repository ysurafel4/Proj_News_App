package com.example.news_app

import NewsAdapter
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale

class MapsGoogleActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var confirmButton: Button
    private var currentMarker: Marker? = null  // Store the current marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_google)

        //set up RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recylerviewMaps)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        newsAdapter = NewsAdapter()
        recyclerView.adapter = newsAdapter

        confirmButton = findViewById(R.id.confirmBtn)

        // Initialize the map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Default news for "us"
        fetchNewsForCountry("us")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapLongClickListener { latLng ->
            updateMarkerAndFetchNews(latLng)
        }
    }

    private fun updateMarkerAndFetchNews(latLng: LatLng) {
        //clear previous marker
        currentMarker?.remove()

        // Get country name from coordinates
        lifecycleScope.launch(Dispatchers.IO) {
            val countryName = getCountryName(latLng)
            withContext(Dispatchers.Main) {
                if (countryName != null) {
                    // Update the marker
                    currentMarker = mMap.addMarker(
                        MarkerOptions().position(latLng).title("News for $countryName")
                    )
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))

                    // Update the button text
                    confirmButton.text = "News for $countryName"

                    //Fetch news
                    fetchNewsForCountry(countryName)
                } else {
                    Toast.makeText(this@MapsGoogleActivity, "Could not get country name", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun getCountryName(latLng: LatLng): String? {
        return withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(this@MapsGoogleActivity, Locale.getDefault())
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (addresses.isNullOrEmpty()) null else addresses[0].countryCode
            } catch (e: IOException) {
                Log.e("Geocoder", "Error getting country name", e)
                null
            }
        }
    }

    private fun fetchNewsForCountry(countryCode: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val articles = NewsApiService.fetchNewsByCountry(countryCode)

                withContext(Dispatchers.Main) {
                    if (articles.isNotEmpty()) {
                        newsAdapter.updateNews(articles)
                    } else {
                        Log.w("API_RESPONSE", "No news found for $countryCode")
                        Toast.makeText(this@MapsGoogleActivity, "No news found for $countryCode", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error fetching news: ${e.message}")
            }
        }
    }
}
