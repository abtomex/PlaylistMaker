package dom.dima.practicum.playlistmaker.media.ui.fragment.playlists.dictionary

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dom.dima.practicum.playlistmaker.databinding.ViewPlaylistInPlaylistsBinding
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist

class PlaylistsAdapter(
    private val onPlaylistClick: (Playlist) -> Unit,
    private val tracksCountDescriptor: (Int) -> String
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    private val items = mutableListOf<Playlist>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Playlist>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = ViewPlaylistInPlaylistsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaylistViewHolder(binding, onPlaylistClick, tracksCountDescriptor)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}