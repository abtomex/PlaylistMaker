package dom.dima.practicum.playlistmaker.media.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
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

    private lateinit var adapter: PlaylistAdapter

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

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.playlistsItems.layoutManager = layoutManager

        binding.playlistsItems.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount = 2,
                spacingPx = resources.getDimensionPixelSize(R.dimen.grid_spacing_8)
            )
        )
        adapter = PlaylistAdapter { playlist ->
            navigateToPlaylistFragment(playlist)
        }
        binding.playlistsItems.adapter = adapter

    }

    private fun navigateToPlaylistFragment(playlist: Playlist) {
        val bundle = Bundle().apply {
            putInt("playlistId", playlist.id)
        }
/*
        findNavController().navigate(
            R.id.action_mediaFragment_to_playlistFragment, bundle
        )
*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

}