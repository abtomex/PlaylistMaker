package dom.dima.practicum.playlistmaker.player.ui.activity

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.databinding.ViewPlaylistBinding
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import dom.dima.practicum.playlistmaker.utils.Useful

class AudioplayerPlaylistsViewHolder(
    val binding: ViewPlaylistBinding,
    private val onPlaylistClick: (Playlist) -> Unit,
    private val tracksCountDescriptor: (Int) -> String
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {
        binding.playlistTitle.text = playlist.title

        binding.tracksCount.text = tracksCountDescriptor(playlist.trackIds.size)

        val radius = Useful.dpToPx(2f, itemView.context)

        try {
            Glide.with(itemView.context).load(playlist.cover)
                .placeholder(R.drawable.ic_no_image_placeholder_45)
                .transform(MultiTransformation(CenterCrop(), RoundedCorners(radius)))
                .into(binding.playlistCover)
        } catch (e: Exception) {
            e.printStackTrace()
            loadDefaultCover(radius)
        }

        itemView.setOnClickListener {
            onPlaylistClick(playlist)
        }
    }

    private fun loadDefaultCover(radius: Int) {
        Glide.with(itemView.context).load(R.drawable.ic_no_image_placeholder_45)
            .transform(MultiTransformation(CenterCrop(), RoundedCorners(radius)))
            .into(binding.playlistCover)
    }


}
