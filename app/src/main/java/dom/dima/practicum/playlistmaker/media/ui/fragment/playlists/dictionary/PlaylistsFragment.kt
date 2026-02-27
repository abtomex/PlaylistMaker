package dom.dima.practicum.playlistmaker.media.ui.fragment.playlists.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import dom.dima.practicum.playlistmaker.media.ui.fragment.playlists.playlist_screen.PlaylistScreenFragment
import dom.dima.practicum.playlistmaker.media.ui.view_model.PlaylistsViewModel
import dom.dima.practicum.playlistmaker.utils.GridSpacingItemDecoration
import dom.dima.practicum.playlistmaker.utils.Useful
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistsViewModel by viewModel()

    private lateinit var adapter: PlaylistsAdapter

    private enum class ElementsVisibility {
        NOT_PLAYLISTS,
        THERE_IS_AT_LEAST_ONE_PLAYLIST
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val buttonNew = binding.btnNew
        buttonNew.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_newPlaylistFragment2)
        }

        binding.playlistsItems.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount = 2,
                spacingPx = resources.getDimensionPixelSize(R.dimen.grid_spacing_8)
            )
        )
        adapter = PlaylistsAdapter (
            onPlaylistClick = { playlist -> navigateToPlaylistFragment(playlist) },
            tracksCountDescriptor = { tracksCount -> Useful.trackItemsText (
                tracksCount,
                getString(R.string.track_items_count_variant1, tracksCount),
                getString(R.string.track_items_count_variant2, tracksCount),
                getString(R.string.track_items_count_variant3, tracksCount)
            )}

        )

        binding.playlistsItems.adapter = adapter
        viewModel.initPlaylistsList()

        viewModel.getPlaylistsState().observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                setupVisibility(ElementsVisibility.NOT_PLAYLISTS)
            } else {
                adapter.submitList(it)
                setupVisibility(ElementsVisibility.THERE_IS_AT_LEAST_ONE_PLAYLIST)
            }
        }

    }


    private fun setupVisibility(state: ElementsVisibility) {
        when (state) {
            ElementsVisibility.NOT_PLAYLISTS -> {
                binding.playlistsItems.isVisible = false
                binding.nothingIcon.isVisible = true
                binding.nothingText.isVisible = true
            }
            ElementsVisibility.THERE_IS_AT_LEAST_ONE_PLAYLIST -> {
                binding.playlistsItems.isVisible = true
                binding.nothingIcon.isVisible = false
                binding.nothingText.isVisible = false

            }
        }

    }

    private fun navigateToPlaylistFragment(playlist: Playlist) {
        findNavController().navigate(
            R.id.action_mediaFragment_to_playlistScreenFragment,
            PlaylistScreenFragment.createArgs(playlist.id)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

}