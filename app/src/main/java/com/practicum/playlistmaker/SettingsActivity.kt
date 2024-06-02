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
            val i = Intent(Intent.ACTION_SENDTO)
            i.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name))
            i.type = "text/plain"
            startActivity( i )
        }

        // button Support
        val btnSupport = findViewById<View>(R.id.btn_support)
        btnSupport.setOnClickListener {
            val i = Intent(Intent.ACTION_SENDTO)
            i.data = Uri.parse("mailto:")
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_mail_subject))
            i.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_mail_to)))
            i.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_mail_text))
            startActivity( i )
        }

        // button License
        val btnLicense = findViewById<View>(R.id.btn_license)
        btnLicense.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse( getString(R.string.offer_url) )
            startActivity( i )
        }

        // button Назад
//        val btnBackToMainActivity = findViewById<ImageButton>(R.id.btn_back_to_main)
//        btnBackToMainActivity.setOnClickListener {
//            startActivity(
//                Intent(this, MainActivity::class.java)
//            )
//        }


    }


}