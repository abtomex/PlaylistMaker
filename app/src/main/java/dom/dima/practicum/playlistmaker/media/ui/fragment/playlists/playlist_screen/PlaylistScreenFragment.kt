package dom.dima.practicum.playlistmaker.media.ui.fragment.playlists.playlist_screen

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.databinding.FragmentPlaylistScreenBinding
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import dom.dima.practicum.playlistmaker.media.ui.fragment.playlists.PlaylistEditorFragment
import dom.dima.practicum.playlistmaker.media.ui.fragment.playlists.state.PlaylistScreenState
import dom.dima.practicum.playlistmaker.media.ui.view_model.PlaylistScreenViewModel
import dom.dima.practicum.playlistmaker.player.ui.activity.AudioPlayerFragment
import dom.dima.practicum.playlistmaker.utils.Useful
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

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

        val playlistId = requireArguments().getInt(CLICKED_PLAYLIST_ID)
        view.post {
            viewModel.loadPlaylistData(playlistId)
        }

        binding.actionBack.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getPlaylistState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistScreenState.LoadData -> {
                    val playlist = state.data
                    setupStandardBottomSheet(playlist)
                    setupMenuMoreBottomSheet(playlist)
                    initPlaylistButtons(playlist)
                    rewritePlaylistPage(playlist)
                }

                is PlaylistScreenState.ReloadData -> {
                    rewritePlaylistPage(state.data)
                }

                is PlaylistScreenState.PlaylistRemoved -> {
                    findNavController().popBackStack()
                }
            }
        }

    }

    fun initPlaylistButtons(playlist: Playlist) {
        binding.buttonShare.setOnClickListener {
            sharePlaylist(playlist)
        }

        binding.buttonMore.setOnClickListener {
            binding.menuMoreBottomSheet.isVisible = true
            val menuMoreBottomSheetBehavior = BottomSheetBehavior.from(binding.menuMoreBottomSheet)
            menuMoreBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            menuMoreBottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> binding.overlay.isVisible = false
                        else -> binding.overlay.isVisible = true
                    }

                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }

    }

    fun sharePlaylist(playlist: Playlist) {
        if (playlist.tracks.isEmpty()) {
            Toast.makeText(
                requireContext(),
                getString(R.string.playlist_has_nothing_tracks),
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val messageBuilder = StringBuilder()
        messageBuilder.append(
            getString(
                R.string.message_playlist_title,
                playlist.title,
                playlist.description
            )
        ).append("\n")
        messageBuilder.append(
            getString(
                R.string.message_playlist_tracks_count,
                playlist.tracks.size
            )
        ).append("\n")
        for (i in 0..<playlist.tracks.size) {
            val track = playlist.tracks[i]
            messageBuilder.append("${i + 1}. ").append("${track.artistName} - ")
                .append("${track.trackName} ")
                .append(
                    "(${
                        SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(track.trackTimeMillis)
                    })"
                )
            messageBuilder.append("\n")
        }
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            setType("text/plain")
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(Intent.EXTRA_TEXT, messageBuilder.toString())
        }
        requireContext().startActivity(shareIntent)

    }

    fun rewritePlaylistPage(playlist: Playlist) {

        binding.playlistTitle.text = playlist.title
        binding.playlistDescription.text = playlist.description
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

    private fun setupStandardBottomSheet(playlist: Playlist) {

        setupBottomSheetMargin(
            binding.standardBottomSheet,
            binding.buttonMore,
            24f,
            BottomSheetBehavior.STATE_COLLAPSED
        )
        setupRecyclerView(playlist)
    }

    private fun setupMenuMoreBottomSheet(playlist: Playlist) {

        setupBottomSheetMargin(
            binding.menuMoreBottomSheet,
            binding.playlistTitle,
            5f,
            BottomSheetBehavior.STATE_HIDDEN
        )
        setupPlaylistViews(playlist)
        binding.menuMoreShare.setOnClickListener {
            sharePlaylist(playlist)
        }

        binding.menuMoreDeletePlaylist.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage(getString(R.string.ask_to_delete_playlist, playlist.title))
                .setNegativeButton(getString(R.string.message_no)) { _, _ -> }
                .setPositiveButton(getString(R.string.message_yes)) { _, _ ->
                    viewModel.deletePlaylist(playlist)
                }
                .show()

        }
        binding.menuMoreEdit.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistScreenFragment_to_editPlaylistFragment,
                PlaylistEditorFragment.createArgs(playlist.id)
            )
        }
    }

    private fun setupBottomSheetMargin(
        bottomSheet: View,
        viewFromWhichMargin: View,
        margin: Float,
        defaultStartState: Int
    ) {
        val actionBackLocation = IntArray(2)
        binding.actionBack.getLocationInWindow(actionBackLocation)
        val zeroLine = actionBackLocation[1]
        val screenHeight = binding.playlistDescriptionConstraintLayout.height
        val absoluteScreenHeight = screenHeight + zeroLine

        val viewFromWhichMarginLocation = IntArray(2)
        viewFromWhichMargin.getLocationInWindow(viewFromWhichMarginLocation)
        val viewFromWhichMarginY = viewFromWhichMarginLocation[1]
        val viewFromWhichMarginHeight = viewFromWhichMargin.height

        val behavior = BottomSheetBehavior.from(bottomSheet)

        val margin24dp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            margin,
            resources.displayMetrics
        ).toInt()

        behavior.apply {
            state = defaultStartState
            peekHeight =
                absoluteScreenHeight - viewFromWhichMarginY - viewFromWhichMarginHeight - margin24dp
        }

    }

    private fun setupPlaylistViews(playlist: Playlist) {
        binding.menuMorePlaylistName.text = playlist.title
        binding.menuMoreTracksCount.text = Useful.itemsText(
            playlist.tracks.size,
            getString(R.string.track_items_count_variant1, playlist.tracks.size),
            getString(R.string.track_items_count_variant2, playlist.tracks.size),
            getString(R.string.track_items_count_variant3, playlist.tracks.size),
        )

        Glide.with(binding.menuMorePlaylistIcon)
            .load(playlist.cover)
            .fitCenter()
            .placeholder(R.drawable.ic_no_image_placeholder_45)
            .transform(RoundedCorners(Useful.dpToPx(2.0f, requireContext())))
            .into(binding.menuMorePlaylistIcon)

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