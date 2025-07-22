package dom.dima.practicum.playlistmaker.rvcomponents

import android.content.Context
import android.content.Intent
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import dom.dima.practicum.playlistmaker.ApplicationConstants
import dom.dima.practicum.playlistmaker.AudioPlayerActivity
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.data.Track
import dom.dima.practicum.playlistmaker.service.SearchHistoryService
import java.text.SimpleDateFormat
import java.util.Locale
import android.os.Handler
import android.os.Looper


class TrackViewHolder(
    parent: ViewGroup,
    trackList: List<Track>,
    private val searchHistoryService: SearchHistoryService
    ) :
    ApplicationConstants,
    RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.view_track, parent, false)
) {

    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val trackIcon: ImageView = itemView.findViewById(R.id.trackIcon)
    private val trackArtistName: TextView = itemView.findViewById(R.id.trackArtistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
    private val gson: Gson = Gson()

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    init {
        itemView.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION && clickDebounce()) {
                val clickedItem = trackList[position]
                showPlayerActivity(itemView.context, clickedItem)
                searchHistoryService.addToHistory(clickedItem)
            }
        }
    }

    private fun showPlayerActivity(context: Context?, clickedItem: Track) {
        val playerIntent = Intent(context, AudioPlayerActivity::class.java)
            .putExtra(CLICKED_TRACK_CONTENT, gson.toJson(clickedItem))

        context?.startActivity(playerIntent)
    }

    fun bind(model : Track) {
        trackName.text = model.trackName
        trackArtistName.text = ""
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
            context.resources.displayMetrics).toInt()
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}