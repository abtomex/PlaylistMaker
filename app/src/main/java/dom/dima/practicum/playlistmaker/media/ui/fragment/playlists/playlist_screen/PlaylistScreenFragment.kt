package dom.dima.practicum.playlistmaker.media.ui.fragment.playlists.playlist_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.databinding.FragmentPlaylistScreenBinding
import dom.dima.practicum.playlistmaker.media.ui.view_model.PlaylistScreenViewModel
import dom.dima.practicum.playlistmaker.player.ui.activity.AudioPlayerFragment
import dom.dima.practicum.playlistmaker.utils.Useful
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistScreenFragment : Fragment() {

    private var _binding: FragmentPlaylistScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistScreenViewModel by viewModel()

    private lateinit var playlistScreenAdapter: PlaylistScreenAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        requireView().setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.playlist_screen_background_color))
        val playlistId = requireArguments().getInt(CLICKED_PLAYLIST_ID)
        viewModel.loadPlaylistData(playlistId)

        setupRecyclerView()
        binding.actionBack.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getPlaylistState().observe(viewLifecycleOwner) {
            binding.playlistTitle.text = it.title
            val tracksCount = it.trackIds.size
            binding.tracksCount.text = Useful.trackItemsText(
                tracksCount,
                getString(R.string.track_items_count_variant1, tracksCount),
                getString(R.string.track_items_count_variant2, tracksCount),
                getString(R.string.track_items_count_variant3, tracksCount)
            )
            Glide.with(this)
                .load(it.cover)
                .fitCenter()
                .placeholder(R.drawable.ic_no_image_placeholder_45)
                .transform(RoundedCorners(Useful.dpToPx(8.0f, requireActivity())))
                .into(binding.playlistCover)
            playlistScreenAdapter.submitList(it.tracks)
        }
    }

    private fun setupRecyclerView() {
        playlistScreenAdapter = PlaylistScreenAdapter(
            mutableListOf(),
            onTrackClick = { track ->
                findNavController().navigate(
                    R.id.action_playlistScreenFragment_to_audioPlayerFragment,
                    AudioPlayerFragment.createArgs(viewModel.toJson(track))
                )

            }

        )
        binding.playlistsRecyclerView.adapter = playlistScreenAdapter
    }

    companion object {
        const val CLICKED_PLAYLIST_ID = "playlistId"

        fun createArgs(playlistId: Int): Bundle =
            bundleOf(CLICKED_PLAYLIST_ID to playlistId)
    }

}