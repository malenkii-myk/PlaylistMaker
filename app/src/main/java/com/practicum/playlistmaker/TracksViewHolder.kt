package com.practicum.playlistmaker

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dpToPx

class TracksViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
    private val artImg: ImageView = itemView.findViewById(R.id.artImg)

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.getFormattedTrackTime()

        if (model.artworkUrl100.isNotEmpty()) {
            Glide.with(itemView)
                .load(model.artworkUrl100)
                .centerInside()
                .transform(RoundedCorners( dpToPx(itemView.context.resources.getDimension(R.dimen.tracklist_art_radius), itemView.context)))
                .placeholder(R.drawable.track_placeholder)
                .into(artImg)
        } else {
            Glide.with(itemView)
                .load(R.drawable.track_placeholder)
                .into(artImg)
        }

    }

}