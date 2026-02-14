package dom.dima.practicum.playlistmaker.media.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dom.dima.practicum.playlistmaker.databinding.FragmentFavoriteTracksBinding
import dom.dima.practicum.playlistmaker.media.view_model.FavoriteTracksViewModel
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesTracksFragment : Fragment() {
    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoriteTracksViewModel by viewModel()

    private val tracks = mutableListOf<Track>()

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupRecyclerView()
        viewModel.getFavoriteState().observe(viewLifecycleOwner) { tracks ->
            if (tracks != null && !tracks.isEmpty()) {
                this.tracks.clear()
                this.tracks.addAll(tracks)
                binding.favoritesRecyclerView.visibility = View.VISIBLE
                binding.nothingIcon.visibility = View.GONE
                binding.nothingText.visibility = View.GONE
                binding.favoritesRecyclerView.adapter?.notifyDataSetChanged()
            } else {
                binding.favoritesRecyclerView.visibility = View.GONE
                binding.nothingIcon.visibility = View.VISIBLE
                binding.nothingText.visibility = View.VISIBLE
            }

        }

    }

    private fun setupRecyclerView() {
        val mediaTrackAdapter = FavoritesTrackAdapter(
            tracks,
            viewModel,
            findNavController()

            )
        binding.favoritesRecyclerView.adapter = mediaTrackAdapter
        viewModel.loadFavorites()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FavoritesTracksFragment()
    }
}