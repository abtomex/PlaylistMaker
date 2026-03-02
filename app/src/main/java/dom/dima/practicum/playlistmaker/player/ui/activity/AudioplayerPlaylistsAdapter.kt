package dom.dima.practicum.playlistmaker.player.ui.activity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dom.dima.practicum.playlistmaker.databinding.ViewPlaylistBinding
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist

class AudioplayerPlaylistsAdapter(
    private val playlists : MutableList<Playlist>,
    private val onPlaylistClick: (Playlist) -> Unit,
    private val tracksCountDescriptor: (Int) -> String
) : RecyclerView.Adapter<AudioplayerPlaylistsViewHolder>() {


    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Playlist>) {
        playlists.clear()
        playlists.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AudioplayerPlaylistsViewHolder {
        val binding = ViewPlaylistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AudioplayerPlaylistsViewHolder(binding, onPlaylistClick, tracksCountDescriptor)
    }

    override fun onBindViewHolder(
        holder: AudioplayerPlaylistsViewHolder,
        position: Int
    ) {
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}