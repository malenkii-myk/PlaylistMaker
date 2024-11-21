package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import kotlinx.coroutines.Job
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var inputSearchText: EditText
    private lateinit var noResultsView: View
    private lateinit var noInternetView: View
    private lateinit var searchHistoryView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchHistoryRecycler: RecyclerView
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter
    private lateinit var searchHistory: SearchHistory
    private var searchJob: Call<TrackResponse>? = null

    private var searchValue: String = SEARCH_DEF

    private val trackBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(trackBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackService = retrofit.create(trackApi::class.java)
    private var trackAdapter = TrackAdapter(emptyList()) { track ->
        searchHistory.addTrack(track)
        updateSearchHistory()
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra(App.KEY_INTENT_TRACK_DATA, track)
        startActivity(intent)
    }

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

        inputSearchText = findViewById(R.id.input_search)
        val clearButton = findViewById<ImageView>(R.id.btn_search_clear)
        val searchView = findViewById<View>(R.id.search_activity)
        noResultsView = findViewById(R.id.no_results)
        noInternetView = findViewById(R.id.no_internet)

        inputSearchText.setText(searchValue)

        // history list
        searchHistory = SearchHistory((applicationContext as App).sharedPreferences)
        searchHistoryRecycler = findViewById(R.id.recycler_search_history)
        searchHistoryRecycler.layoutManager = LinearLayoutManager(this)
        searchHistoryAdapter = SearchHistoryAdapter(emptyList())
        searchHistoryRecycler.adapter = searchHistoryAdapter
        searchHistoryView = findViewById(R.id.search_history_view)
        updateSearchHistory()

        // track list
        recyclerView = findViewById(R.id.trackList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        trackAdapter.updateTracks(emptyList())
        recyclerView.adapter = trackAdapter


        // clear "X" button
        clearButton.setOnClickListener {
            inputSearchText.setText("")
            searchView.hideKeyboard()
            searchValue = ""
        }

        // button Clear History
        val btnClearHistory = findViewById<Button>(R.id.btn_clear_history)
        btnClearHistory.setOnClickListener {
            searchHistory.clearHistory()
            updateSearchHistory()
            showSearchHistory(false)
        }


        // Input Search Text
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
                searchValue = s.toString().trim()
                showSearchHistory(inputSearchText.hasFocus() && s?.isEmpty() == true)
                searchTracks(searchValue)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputSearchText.addTextChangedListener(simpleTextWatcher)

        inputSearchText.setOnFocusChangeListener { view, hasFocus ->
            updateSearchHistory()
            showSearchHistory(hasFocus && inputSearchText.text.isEmpty())
        }


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
        btnRefresh.setOnClickListener {
            searchTracks(searchValue)
        }


        // button Назад
        val btnBackToMainActivity = findViewById<ImageButton>(R.id.btn_back_to_main)
        btnBackToMainActivity.setOnClickListener {
            finish()
        }

    }


    private fun searchTracks(s: String) {
        searchJob?.cancel()
        if (s.isEmpty() || s.length < App.MIN_LENGTH_SEARCH_QUERY) {
            trackAdapter.updateTracks(emptyList())
            noResultsView.visibility =
                if (s.isEmpty() && inputSearchText.text.isNotEmpty()) View.VISIBLE else View.GONE
            recyclerView.isVisible = false
            noInternetView.isVisible = false
            return
        }
        searchJob = trackService.search(s)
        searchJob?.enqueue(object : retrofit2.Callback<TrackResponse> {
            override fun onResponse(
                call: retrofit2.Call<TrackResponse>,
                response: retrofit2.Response<TrackResponse>
            ) {
                showSearchHistory(false)
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
                searchHistoryView.isVisible = false
            }
        })
    }


    private fun showSearchHistory(isVisible: Boolean) {
        searchHistoryView.visibility = if (isVisible && searchHistoryAdapter.itemCount > 0)
            View.VISIBLE else View.GONE
    }

    private fun updateSearchHistory() {
        val historyList = searchHistory.getHistory()
        searchHistoryAdapter.updateTracks(historyList)
    }


    private companion object {
        const val SEARCH_VALUE = "SEARCH_VALUE"
        const val SEARCH_DEF = ""
    }


}