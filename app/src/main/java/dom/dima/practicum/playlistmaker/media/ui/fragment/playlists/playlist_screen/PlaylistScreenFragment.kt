package dom.dima.practicum.playlistmaker.media.ui.fragment.playlists.playlist_screen

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.databinding.FragmentPlaylistScreen1Binding
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import dom.dima.practicum.playlistmaker.media.ui.fragment.playlists.state.PlaylistScreenState
import dom.dima.practicum.playlistmaker.media.ui.view_model.PlaylistScreenViewModel
import dom.dima.practicum.playlistmaker.player.ui.activity.AudioPlayerFragment
import dom.dima.practicum.playlistmaker.utils.Useful
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistScreenFragment : Fragment() {

    private var _binding: FragmentPlaylistScreen1Binding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistScreenViewModel by viewModel()

    private lateinit var playlistScreenAdapter: PlaylistScreenAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistScreen1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val playlistId = requireArguments().getInt(CLICKED_PLAYLIST_ID)
        view.post {
            viewModel.loadPlaylistData(playlistId)
        }

        binding.actionBack.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getPlaylistState().observe(viewLifecycleOwner) { state ->
            val playlist = state.playlist
            when (state) {
                is PlaylistScreenState.LoadData -> {
                    setupBottomSheet(playlist)
                    rewritePlaylistPage(playlist)
                }
                is PlaylistScreenState.ReloadData -> {
                    rewritePlaylistPage(playlist)
                }
            }
        }

        binding.buttonShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                setType("text/plain")
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra(Intent.EXTRA_TEXT, "трек 1, трек 2, трек 3")
            }
            requireContext().startActivity(shareIntent)
        }
    }

    fun rewritePlaylistPage(playlist: Playlist) {

        binding.playlistTitle.text = playlist.title
        val tracksCount = playlist.trackIds.size

        binding.tracksCount.text = Useful.itemsText(
            tracksCount,
            getString(R.string.track_items_count_variant1, tracksCount),
            getString(R.string.track_items_count_variant2, tracksCount),
            getString(R.string.track_items_count_variant3, tracksCount)
        )
        Glide.with(this)
            .load(playlist.cover)
            .fitCenter()
            .placeholder(R.drawable.ic_no_image_placeholder_45)
            .transform(RoundedCorners(Useful.dpToPx(8.0f, requireActivity())))
            .into(binding.playlistCover)
        val totalDuration =
            ((playlist.tracks.sumOf { trackDuration -> trackDuration.trackTimeMillis }) / 1000 / 60).toInt()

        binding.tracksTotalDurability.text = Useful.itemsText(
            totalDuration,
            getString(R.string.minutes_items_count_variant1, totalDuration),
            getString(R.string.minutes_items_count_variant2, totalDuration),
            getString(R.string.minutes_items_count_variant3, totalDuration)
        )
        playlistScreenAdapter.submitList(playlist.tracks)

    }

    private fun setupBottomSheet(playlist: Playlist) {

        setupBottomSheetMargin()
        setupRecyclerView(playlist)
    }

    private fun setupBottomSheetMargin() {
        val actionBackLocation = IntArray(2)
        binding.actionBack.getLocationInWindow(actionBackLocation)
        val zeroLine = actionBackLocation[1]
        val screenHeight = binding.playlistDescriptionConstraintLayout.height
        val absoluteScreenHeight = screenHeight + zeroLine

        val buttonMoreLocation = IntArray(2)
        binding.buttonMore.getLocationInWindow(buttonMoreLocation)
        val buttonMoreY = buttonMoreLocation[1]
        val buttonMoreHeight = binding.buttonMore.height

        val bottomSheet = binding.standardBottomSheet
        val behavior = BottomSheetBehavior.from(bottomSheet)

        val margin24dp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            24f,
            resources.displayMetrics
        ).toInt()

        behavior.apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
            peekHeight = absoluteScreenHeight - buttonMoreY - buttonMoreHeight - margin24dp
        }

    }


    private fun setupRecyclerView(playlist: Playlist) {
        playlistScreenAdapter = PlaylistScreenAdapter(
            mutableListOf(),
            onTrackClick = { track ->
                findNavController().navigate(
                    R.id.action_playlistScreenFragment_to_audioPlayerFragment,
                    AudioPlayerFragment.createArgs(viewModel.toJson(track))
                )
            },
            onTrackLongClick = { track ->
                MaterialAlertDialogBuilder(requireContext())
                    .setMessage(getString(R.string.ask_to_delete_track_message))
                    .setNegativeButton(getString(R.string.message_no)) { _, _ -> }
                    .setPositiveButton(getString(R.string.message_yes)) { _, _ ->
                        viewModel.removeTrackFromPlaylist(track, playlist)
                    }
                    .show()
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