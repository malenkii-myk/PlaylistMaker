package com.practicum.playlistmaker

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dpToPx

class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val track = intent.getSerializableExtra(App.KEY_INTENT_TRACK_DATA) as Track

        val trackNameText = findViewById<TextView>(R.id.track_name)
        val artistNameText = findViewById<TextView>(R.id.artist_name)
        val trackTimeText = findViewById<TextView>(R.id.track_length)
        val collectionNameText = findViewById<TextView>(R.id.album)
        val primaryGenreNameText = findViewById<TextView>(R.id.genre)
        val releaseDateText = findViewById<TextView>(R.id.year)
        val countryText = findViewById<TextView>(R.id.country)
        val artwork = findViewById<ImageView>(R.id.artwork)

        trackNameText.text = track.trackName
        artistNameText.text = track.artistName
        trackTimeText.text = track.getFormattedTrackTime()
        collectionNameText.text = track.collectionName
        releaseDateText.text = track.releaseDate.substring(0, 4)
        primaryGenreNameText.text = track.primaryGenreName
        countryText.text = track.country

        Glide.with(this)
            .load(track.getCoverArtwork())
            .centerCrop()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.player_art_radius)))
            .placeholder(R.drawable.track_placeholder_big)
            .into(artwork)

        // button Назад
        val btnBackToMainActivity = findViewById<ImageButton>(R.id.btn_back_to_main)
        btnBackToMainActivity.setOnClickListener {
            finish()
        }
    }
}