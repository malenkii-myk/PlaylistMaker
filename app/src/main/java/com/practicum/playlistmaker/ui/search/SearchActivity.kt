package com.practicum.playlistmaker.ui.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.api.TrackInteractor
import com.practicum.playlistmaker.domain.model.Resource
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.ui.PlayerActivity
import hideKeyboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private lateinit var inputSearchText: EditText
    private lateinit var noResultsView: View
    private lateinit var noInternetView: View
    private lateinit var searchHistoryView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchHistoryRecycler: RecyclerView
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter
    private lateinit var searchHistoryInteractor: SearchHistoryInteractor
    private lateinit var progressBar: ProgressBar

    private var searchValue: String = SEARCH_DEF


    private val trackInteractor: TrackInteractor = Creator.provideTrackInteractor()
    private var trackAdapter = TrackAdapter(emptyList()) { track ->
        if (clickDebounce()) {
            searchHistoryInteractor.addTrack(track)
            updateSearchHistory()
            clickTrack(track)
        }
    }

    // Debounce
    private var isClickAllowed = true
    private val clickHandler = Handler(Looper.getMainLooper())
    private val searchHandler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchTracksAPI(searchValue) }

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

        progressBar = findViewById(R.id.progress_bar)

        // history list
        searchHistoryInteractor = Creator.provideSearchHistoryInteractor()
        searchHistoryRecycler = findViewById(R.id.recycler_search_history)
        searchHistoryRecycler.layoutManager = LinearLayoutManager(this)
        searchHistoryAdapter = SearchHistoryAdapter(emptyList()) { track ->
            searchHistoryInteractor.addTrack(track)
            updateSearchHistory()
            clickTrack(track)
        }
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
            searchHistoryInteractor.clearHistory()
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
                if (searchValue.isEmpty() || searchValue.length < App.MIN_LENGTH_SEARCH_QUERY) {
                    searchHandler.removeCallbacks(searchRunnable)
                    progressBar.isVisible = false
                    noResultsView.isVisible = false
                    noInternetView.isVisible = false
                    recyclerView.isVisible = false
                    showSearchHistory(inputSearchText.hasFocus() && searchValue.isEmpty())
                    return
                }
                progressBar.isVisible = true
                noResultsView.isVisible = false
                noInternetView.isVisible = false
                recyclerView.isVisible = false
                searchDebounce()
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
                searchHandler.removeCallbacks(searchRunnable)
                searchTracksAPI(searchValue)
                searchView.hideKeyboard()
                true
            }
            false
        }

        // button Refresh
        val btnRefresh = findViewById<Button>(R.id.btn_refresh)
        btnRefresh.setOnClickListener {
            searchHandler.removeCallbacks(searchRunnable)
            searchTracksAPI(searchValue)
        }


        // button Назад
        val btnBackToMainActivity = findViewById<ImageButton>(R.id.btn_back_to_main)
        btnBackToMainActivity.setOnClickListener {
            finish()
        }

    }

    override fun onPause() {
        super.onPause()
        searchHandler.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        searchHandler.removeCallbacksAndMessages(null)
    }

    private fun clickTrack(track: Track){
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra(App.KEY_INTENT_TRACK_DATA, track)
        startActivity(intent)
    }

    private fun searchTracksAPI(s: String) {
        if (s.isEmpty() || s.length < App.MIN_LENGTH_SEARCH_QUERY) return
        showSearchHistory(false)
        noInternetView.isVisible = false
        noResultsView.isVisible = false
        progressBar.isVisible = true


        trackInteractor.search(s, object : TrackInteractor.TrackConsumer {
            override fun consume(trackList: Resource<List<Track>>) {
                CoroutineScope(Dispatchers.Main).launch { // без этой штуки приложение крашится ;((
                    progressBar.isVisible = false
                    when (trackList) {
                        is Resource.Success -> {
                            if (trackList.data.isNotEmpty()) {
                                trackAdapter.updateTracks(trackList.data)
                                noResultsView.isVisible = false
                                recyclerView.isVisible = true
                                noInternetView.isVisible = false
                            } else {
                                noResultsView.isVisible = true
                                recyclerView.isVisible = false
                                noInternetView.isVisible = false
                            }
                        }
                        is Resource.Fail -> {
                            progressBar.isVisible = false
                            noResultsView.isVisible = false
                            recyclerView.isVisible = false
                            noInternetView.isVisible = true
                            searchHistoryView.isVisible = false
                        }
                    }
                }
            }
        })

    }

    private fun showSearchHistory(isVisible: Boolean) {
        searchHistoryView.visibility = if (isVisible && searchHistoryAdapter.itemCount > 0)
            View.VISIBLE else View.GONE
    }

    private fun updateSearchHistory() {
        val historyList = searchHistoryInteractor.getHistory()
        searchHistoryAdapter.updateTracks(historyList)
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            clickHandler.postDelayed({ isClickAllowed = true }, App.CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        searchHandler.removeCallbacks(searchRunnable)
        searchHandler.postDelayed(searchRunnable, App.SEARCH_DEBOUNCE_DELAY)
    }


    private companion object {
        const val SEARCH_VALUE = "SEARCH_VALUE"
        const val SEARCH_DEF = ""
    }


}