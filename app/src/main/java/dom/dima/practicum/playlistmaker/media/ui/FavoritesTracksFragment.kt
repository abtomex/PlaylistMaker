package dom.dima.practicum.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dom.dima.practicum.playlistmaker.databinding.FragmentFavoriteTracksBinding
import dom.dima.practicum.playlistmaker.media.view_model.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesTracksFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteTracksBinding
    private val viewModel: FavoriteTracksViewModel by viewModel()

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = FavoritesTracksFragment()
    }
}