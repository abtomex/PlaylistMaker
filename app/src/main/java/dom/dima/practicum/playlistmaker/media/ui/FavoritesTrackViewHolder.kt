package dom.dima.practicum.playlistmaker.media.ui

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.media.view_model.FavoriteTracksViewModel
import dom.dima.practicum.playlistmaker.player.ui.activity.AudioPlayerFragment
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class FavoritesTrackViewHolder(
    parent: ViewGroup,
    trackList: List<Track>,
    private val viewModel: FavoriteTracksViewModel,
    private val navController: NavController,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.view_track, parent, false)
) {
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val trackIcon: ImageView = itemView.findViewById(R.id.trackIcon)
    private val trackArtistName: TextView = itemView.findViewById(R.id.trackArtistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)

    init {
        itemView.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                handleClick(position, trackList)
            }
        }
    }

    private fun handleClick(position: Int, trackList: List<Track>) {

        if (viewModel.clickIsAllowed) {
            viewModel.clickIsAllowed = false
            val clickedItem = trackList[position]
            viewModel.clickDebounce {
                showPlayer(clickedItem)
            }
        }
    }

    private fun showPlayer(clickedItem: Track) {
        navController.navigate(
            R.id.action_mediaFragment_to_audioPlayerFragment,
            AudioPlayerFragment.createArgs(viewModel.gson().toJson(clickedItem))
        )
    }

    fun bind(model: Track) {
        trackName.text = model.trackName
        trackArtistName.text = model.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .fitCenter()
            .placeholder(R.drawable.ic_no_image_placeholder_45)
            .transform(RoundedCorners(dpToPx(2.0f, itemView.context)))
            .into(trackIcon)
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }


}