package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchHistoryAdapter(
    private var tracks: List<Track>,
    private val callback: (Track) -> Unit
) : RecyclerView.Adapter<TracksViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TracksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            callback(tracks[position])
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun updateTracks(newTrackList: List<Track>) {
        tracks = newTrackList
        notifyDataSetChanged()
    }

}