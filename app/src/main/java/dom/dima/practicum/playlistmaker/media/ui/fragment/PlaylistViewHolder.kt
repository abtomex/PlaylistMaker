package dom.dima.practicum.playlistmaker.media.ui.fragment

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.databinding.PlaylistItemBinding
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import dom.dima.practicum.playlistmaker.utils.Useful
import java.io.File

class PlaylistViewHolder(
    private val binding: PlaylistItemBinding,
    private val onPlaylistClick: (Playlist) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Playlist) {
        binding.playlistTitle.text = item.title

        val tracksCount = item.trackIds.size
        binding.tracksCount.text = tracksCount.toString()

        val radius = Useful.dpToPx(8f, itemView.context)

        if (!item.coverImgName.isNullOrEmpty()) {
            try {
                val file = File(item.coverImgName)
                if (file.exists()) {
                    Glide.with(itemView.context).load(file)
                        .placeholder(R.drawable.ic_no_image_placeholder_45)
                        .transform(MultiTransformation(CenterCrop(), RoundedCorners(radius)))
                        .into(binding.playlistCover)
                } else {
                    loadDefaultCover(radius)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                loadDefaultCover(radius)
            }
        } else {
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