package dom.dima.practicum.playlistmaker.media.ui.fragment.playlists.playlist_screen

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.databinding.ViewTrackBinding
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import dom.dima.practicum.playlistmaker.utils.Useful
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistScreenViewHolder(val binding: ViewTrackBinding, val onTrackClick: (Track) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) {
        binding.trackName.text = track.trackName
        binding.trackArtistName.text = track.artistName
        binding.trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .fitCenter()
            .placeholder(R.drawable.ic_no_image_placeholder_45)
            .transform(RoundedCorners(Useful.dpToPx(2.0f, itemView.context)))
            .into(binding.trackIcon)

        itemView.setOnClickListener {
            onTrackClick(track)
        }
    }
}
