package com.practicum.playlistmaker.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.search.SearchActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSearch = findViewById<Button>(R.id.btn_search)
        val btnLibrary = findViewById<Button>(R.id.btn_library)
        val btnSettings = findViewById<Button>(R.id.btn_settings)

        // button поиск
        btnSearch.setOnClickListener {
            startActivity(
                Intent(this, SearchActivity::class.java)
            )
        }

        // button медиатека
        btnLibrary.setOnClickListener {
            startActivity(
                Intent(this, LibraryActivity::class.java)
            )
        }

        // button настройки
        btnSettings.setOnClickListener {
            startActivity(
                Intent(this, SettingsActivity::class.java)
            )
        }
    }
}