package dom.dima.practicum.playlistmaker.media.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import dom.dima.practicum.playlistmaker.media.ui.view_model.PlaylistsViewModel
import dom.dima.practicum.playlistmaker.utils.GridSpacingItemDecoration
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
        adapter = PlaylistsAdapter { playlist ->
            navigateToPlaylistFragment(playlist)
        }
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
                binding.playlistsItems.visibility = View.GONE
                binding.nothingIcon.visibility = View.VISIBLE
                binding.nothingText.visibility = View.VISIBLE
            }
            ElementsVisibility.THERE_IS_AT_LEAST_ONE_PLAYLIST -> {
                binding.playlistsItems.visibility = View.VISIBLE
                binding.nothingIcon.visibility = View.GONE
                binding.nothingText.visibility = View.GONE

            }
        }

    }

    private fun navigateToPlaylistFragment(playlist: Playlist) {
        Bundle().apply {
            putInt("playlistId", playlist.id)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

}