package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat

class SettingsActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        // button Share
        val btnShare = findViewById<View>(R.id.btn_share)
        btnShare.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, getString(R.string.app_name)))
        }

        // button Support
        val btnSupport = findViewById<View>(R.id.btn_support)
        btnSupport.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_mail_subject))
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_mail_to)))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_mail_text))
            startActivity( intent )
        }

        // button License
        val btnLicense = findViewById<View>(R.id.btn_license)
        btnLicense.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse( getString(R.string.offer_url) )
            startActivity( intent )
        }

        // button Назад
        val btnBackToMainActivity = findViewById<ImageButton>(R.id.btn_back_to_main)
        btnBackToMainActivity.setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }


    }


}