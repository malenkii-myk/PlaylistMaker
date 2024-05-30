package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat

class SettingsActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        // button Назад
        val btnBackToMainActivity = findViewById<ImageButton>(R.id.btn_back_to_main)
        btnBackToMainActivity.setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }
    }


}