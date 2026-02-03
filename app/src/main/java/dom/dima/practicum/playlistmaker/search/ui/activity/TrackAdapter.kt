package dom.dima.practicum.playlistmaker.search.ui.activity

import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import dom.dima.practicum.playlistmaker.search.ui.view_model.SearchViewModel

class TrackAdapter(
    private val trackList: List<Track>,
    private val viewModel: SearchViewModel,
    private val navController: NavController,
    private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<TrackViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent, trackList, viewModel, navController, lifecycleScope)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

}