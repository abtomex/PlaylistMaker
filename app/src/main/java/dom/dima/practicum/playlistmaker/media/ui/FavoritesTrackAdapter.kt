package dom.dima.practicum.playlistmaker.media.ui

import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import dom.dima.practicum.playlistmaker.media.view_model.FavoriteTracksViewModel
import dom.dima.practicum.playlistmaker.search.domain.models.Track

class FavoritesTrackAdapter (
    private val trackList: List<Track>,
    private val viewModel: FavoriteTracksViewModel,
    private val navController: NavController,
) : RecyclerView.Adapter<FavoritesTrackViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritesTrackViewHolder {
        return FavoritesTrackViewHolder(parent, trackList, viewModel, navController)
    }

    override fun onBindViewHolder(
        holder: FavoritesTrackViewHolder,
        position: Int
    ) {
        holder.bind(trackList[position])
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}