package com.practicum.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dpToPx

class PlayerActivity : AppCompatActivity() {

    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private lateinit var trackUrl: String
    private lateinit var btnPlay: ImageButton
    private lateinit var trackPlayTime: TextView

    private val handler = Handler(Looper.getMainLooper())
    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            if (playerState == STATE_PLAYING) {
                drawCurrentPlayingTime(mediaPlayer.currentPosition.toLong())
                handler.postDelayed(this, App.PLAYER_HANDLER_DELAY)
            }
        }
    }

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
        trackPlayTime = findViewById(R.id.play_time)

        trackNameText.text = track.trackName
        artistNameText.text = track.artistName
        trackTimeText.text = track.getFormattedTrackTime()
        collectionNameText.text = track.collectionName
        releaseDateText.text = track.releaseDate.substring(0, 4)
        primaryGenreNameText.text = track.primaryGenreName
        countryText.text = track.country
        trackUrl = track.previewUrl

        preparePlayer()

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

        // button PLAY
        btnPlay = findViewById(R.id.btn_play)
        btnPlay.setOnClickListener {
            playbackControl()
        }

    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacksAndMessages(null)
    }

    private fun preparePlayer() {
        if (!trackUrl.isNullOrBlank()) {
            mediaPlayer.setDataSource(trackUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                btnPlay.isEnabled = true
                playerState = STATE_PREPARED
                drawCurrentPlayingTime(0)
            }
            mediaPlayer.setOnCompletionListener {
                btnPlay.setImageResource(R.drawable.play_big)
                playerState = STATE_PREPARED
                drawCurrentPlayingTime(0)
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        btnPlay.setImageResource(R.drawable.pause_big)
        playerState = STATE_PLAYING
        handler.post(updateTimeRunnable)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        btnPlay.setImageResource(R.drawable.play_big)
        playerState = STATE_PAUSED
        handler.removeCallbacks(updateTimeRunnable)
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun drawCurrentPlayingTime(time: Long){
        trackPlayTime.text = Track.getFormattedTrackTime(time)
    }

    private companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}