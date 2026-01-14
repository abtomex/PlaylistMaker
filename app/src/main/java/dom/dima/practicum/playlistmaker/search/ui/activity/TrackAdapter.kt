package dom.dima.practicum.playlistmaker.search.ui.activity

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import dom.dima.practicum.playlistmaker.search.domain.models.Track

class TrackAdapter(
    private val trackList: List<Track>,
    private val searchHistoryService: SearchHistoryService,
    private val gson: Gson
) : RecyclerView.Adapter<TrackViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent, trackList, searchHistoryService, gson)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

}