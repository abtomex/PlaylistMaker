package dom.dima.practicum.playlistmaker.search.ui.activity

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.player.ui.activity.AudioPlayerFragment
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import dom.dima.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(
    parent: ViewGroup,
    trackList: List<Track>,
    private val viewModel: SearchViewModel,
    private val navController: NavController,
    private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.view_track, parent, false)
) {

    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val trackIcon: ImageView = itemView.findViewById(R.id.trackIcon)
    private val trackArtistName: TextView = itemView.findViewById(R.id.trackArtistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)

    private var isClickAllowed = true
    private var clickDebounceJob: Job? = null

    init {
        itemView.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                handleClick(position, trackList)
            }
        }
    }

    private fun handleClick(position: Int, trackList: List<Track>) {

        if (isClickAllowed) {
            isClickAllowed = false
            val clickedItem = trackList[position]

            clickDebounceJob?.cancel()
            clickDebounceJob = lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }

            showPlayer(itemView.context, clickedItem)
            viewModel.addToHistory(clickedItem)
        }
    }

    private fun showPlayer(context: Context?, clickedItem: Track) {
        navController.navigate(
            R.id.action_searchFragment_to_audioPlayerFragment,
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


    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}