package dom.dima.practicum.playlistmaker.media.ui.fragment.playlists.playlist_screen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dom.dima.practicum.playlistmaker.databinding.ViewTrackBinding
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import dom.dima.practicum.playlistmaker.search.domain.models.Track

class PlaylistScreenAdapter(
    val tracks: MutableList<Track>,
    val onTrackClick: (Track) -> Unit
) : RecyclerView.Adapter<PlaylistScreenViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistScreenViewHolder {
        val binding = ViewTrackBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaylistScreenViewHolder(binding, onTrackClick)
    }

    override fun onBindViewHolder(
        holder: PlaylistScreenViewHolder,
        position: Int
    ) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Track>) {
        tracks.clear()
        tracks.addAll(list)
        notifyDataSetChanged()
    }
}