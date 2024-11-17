package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hideKeyboard
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var noResultsView: View
    private lateinit var noInternetView: View
    private lateinit var recyclerView: RecyclerView

    private var searchValue: String = SEARCH_DEF

    private val trackBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(trackBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackService = retrofit.create(trackApi::class.java)
    private var trackAdapter = TrackAdapter(emptyList())

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_VALUE, searchValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchValue = savedInstanceState.getString(SEARCH_VALUE, SEARCH_DEF)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val inputSearchText = findViewById<EditText>(R.id.input_search)
        val clearButton = findViewById<ImageView>(R.id.btn_search_clear)
        val searchView = findViewById<View>(R.id.search_activity)
        noResultsView = findViewById(R.id.no_results)
        noInternetView = findViewById(R.id.no_internet)

        inputSearchText.setText(searchValue)

        // track list
        recyclerView = findViewById(R.id.trackList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        trackAdapter.updateTracks(emptyList())
        recyclerView.adapter = trackAdapter


        // clear button
        clearButton.setOnClickListener {
            inputSearchText.setText("")
            searchView.hideKeyboard()
            searchValue=""
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
                searchValue = s.toString().trim()
                searchTracks(searchValue)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputSearchText.addTextChangedListener(simpleTextWatcher)


        // button Done
        inputSearchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTracks(searchValue)
                searchView.hideKeyboard()
                true
            }
            false
        }

        // button Refresh
        val btnRefresh = findViewById<Button>(R.id.btn_refresh)
        btnRefresh.setOnClickListener{
            searchTracks(searchValue)
        }


        // button Назад
        val btnBackToMainActivity = findViewById<ImageButton>(R.id.btn_back_to_main)
        btnBackToMainActivity.setOnClickListener {
            finish()
        }

    }

    private fun searchTracks(s: String){
        if (s.isEmpty() || s.length < 3) {
            trackAdapter.updateTracks(emptyList())
            noResultsView.isVisible = false
            recyclerView.isVisible = false
            noInternetView.isVisible = false
            return
        }

        trackService.search(s).enqueue(object : retrofit2.Callback<TrackResponse> {
            override fun onResponse(call: retrofit2.Call<TrackResponse>, response: retrofit2.Response<TrackResponse>) {
                if (response.isSuccessful) {
                    val trackList = response.body()?.results ?: emptyList()
                    trackAdapter.updateTracks(trackList)
                    noResultsView.isVisible = trackList.isEmpty()
                    recyclerView.isVisible = trackList.isNotEmpty()
                    noInternetView.isVisible = false
                } else {
                    noResultsView.isVisible = false
                    recyclerView.isVisible = false
                    noInternetView.isVisible = true
                }
            }
            override fun onFailure(call: retrofit2.Call<TrackResponse>, t: Throwable) {
                noResultsView.isVisible = false
                recyclerView.isVisible = false
                noInternetView.isVisible = true
            }
        })
    }

    private companion object {
        const val SEARCH_VALUE = "SEARCH_VALUE"
        const val SEARCH_DEF = ""
    }


}