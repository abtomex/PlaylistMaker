package dom.dima.practicum.playlistmaker.media.ui.fragment.playlists.dictionary

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.databinding.ViewPlaylistInPlaylistsBinding
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import dom.dima.practicum.playlistmaker.utils.Useful

class PlaylistViewHolder(
    private val binding: ViewPlaylistInPlaylistsBinding,
    private val onPlaylistClick: (Playlist) -> Unit,
    private val tracksCountDescriptor: (Int) -> String
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Playlist) {
        binding.playlistTitle.text = item.title

        val tracksCount = item.trackIds.size

        binding.tracksCount.text = tracksCountDescriptor(tracksCount)

        val radius = Useful.dpToPx(8f, itemView.context)

        try {
            Glide.with(itemView.context).load(item.cover)
                .placeholder(R.drawable.ic_no_image_placeholder_45)
                .transform(MultiTransformation(CenterCrop(), RoundedCorners(radius)))
                .into(binding.playlistCover)
        } catch (e: Exception) {
            e.printStackTrace()
            loadDefaultCover(radius)
        }

        itemView.setOnClickListener {
            onPlaylistClick(item)
        }
    }

    private fun loadDefaultCover(radius: Int) {
        Glide.with(itemView.context).load(R.drawable.ic_no_image_placeholder_45)
            .transform(MultiTransformation(CenterCrop(), RoundedCorners(radius)))
            .into(binding.playlistCover)
    }
}