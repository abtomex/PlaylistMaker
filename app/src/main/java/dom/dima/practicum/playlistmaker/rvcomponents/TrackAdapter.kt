package dom.dima.practicum.playlistmaker.rvcomponents

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dom.dima.practicum.playlistmaker.data.Track

class TrackAdapter(
    private val trackList: List<Track>
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }
}