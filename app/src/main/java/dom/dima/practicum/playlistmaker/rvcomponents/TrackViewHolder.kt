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
import dom.dima.practicum.playlistmaker.AudioplayerActivity
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.data.Track
import dom.dima.practicum.playlistmaker.service.SearchHistoryService
import java.text.SimpleDateFormat
import java.util.Locale

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

    init {
        itemView.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val clickedItem = trackList[position]
                showPlayerActivity(itemView.context, clickedItem)
                searchHistoryService.addToHistory(clickedItem)
            }
        }
    }

    private fun showPlayerActivity(context: Context?, clickedItem: Track) {
        val playerIntent = Intent(context, AudioplayerActivity::class.java)
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

}