package dom.dima.practicum.playlistmaker.player.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.databinding.FragmentAudioplayerBinding
import dom.dima.practicum.playlistmaker.player.ui.state.AudioPlayerState
import dom.dima.practicum.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import dom.dima.practicum.playlistmaker.utils.Useful
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Objects

@Suppress("DEPRECATION", "INFERRED_TYPE_VARIABLE_INTO_POSSIBLE_EMPTY_INTERSECTION")
class AudioPlayerFragment : Fragment() {

    private var _binding: FragmentAudioplayerBinding? = null
    private val binding get() = _binding!!

    private var playerState = AudioPlayerViewModel.STATE_DEFAULT
    private val viewModel by viewModel<AudioPlayerViewModel>()

    private lateinit var playlistsAdapter: AudioplayerPlaylistsAdapter

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioplayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val trackJson = requireArguments().getString(CLICKED_TRACK_CONTENT) ?: ""
        val track = viewModel.fromJson(trackJson, Track::class.java)
        val trackIcon = binding.cover
        val durability = binding.durabilityVal
        val commonButton = binding.commonButton
        val buttonLike = binding.buttonLikeSwitch

        binding.actionBack.setOnClickListener {
            findNavController().popBackStack()
        }

        setupBottomSheet()

        Glide.with(this)
            .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .fitCenter()
            .placeholder(R.drawable.ic_no_image_placeholder_45)
            .transform(RoundedCorners(Useful.dpToPx(8.0f, requireActivity())))
            .into(trackIcon)
        durability.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        setText(getString(R.string.zero_timer), null, binding.progress)
        setText(track.trackName, null, binding.trackName)
        setText(track.artistName, null, binding.artistName)
        setText(track.collectionName, binding.album, binding.albumVal)
        setText(track.primaryGenreName, binding.genre, binding.genreVal)
        setText(track.country, binding.country, binding.countryVal)

        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val date = track.releaseDate?.let { inputFormat.parse(it) }
            val outputFormat = SimpleDateFormat("yyyy", Locale.getDefault())
            setText(
                outputFormat.format(date!!),
                binding.year,
                binding.yearVal
            )
        } catch (_: Exception) {
            setText(
                "",
                binding.year,
                binding.yearVal
            )
        }

        viewModel.preparePlayer(track.previewUrl)

        commonButton.setOnClickListener {
            playbackControl()
        }

        buttonLike.setOnClickListener {
            viewModel.addToFavoriteOrRemove(track)
        }

        viewModel.initButtonLikeStatus(track)

        viewModel.getPlayerState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is AudioPlayerState.Prepared -> {
                    playerState = state.data.playerState
                    commonButton.isEnabled = true
                }

                is AudioPlayerState.Completion -> {
                    playerState = state.data.playerState
                    isStarted = false
                    viewModel.pausePlayer()
                    binding.progress.text = getString(R.string.zero_timer)
                }

                is AudioPlayerState.Playing -> {
                    playerState = state.data.playerState
                    commonButton.setImageResource(R.drawable.button_pause)
                    isStarted = true
                    binding.progress.text = state.progress

                }

                is AudioPlayerState.Pause -> {
                    playerState = state.data.playerState
                    isStarted = false
                    commonButton.setImageResource(R.drawable.button_play)

                }

                is AudioPlayerState.Favorite -> {
                    buttonLike.setImageResource(R.drawable.button_liked)
                }

                is AudioPlayerState.NotFavorite -> {
                    buttonLike.setImageResource(R.drawable.button_unliked)
                }

                is AudioPlayerState.Playlists -> {
                    playlistsAdapter.submitList(state.playlists)

                }

                is AudioPlayerState.CompleteAddToPlaylist -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.complete_add_to_playlist, state.title),
                        Toast.LENGTH_LONG
                    ).show()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }

                is AudioPlayerState.AlreadyExists -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.track_already_exists_in_playlist, state.title),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }
        binding.buttonAddToPlaylist.setOnClickListener {

            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                viewModel.getPlaylists()
            }
        }

        binding.btnNew.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            findNavController().navigate(R.id.action_audioPlayerFragment_to_newPlaylistFragment)
        }


        setupRecyclerView()

    }

    private fun setupBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)

        bottomSheetBehavior.apply {
            isHideable = true
            state = BottomSheetBehavior.STATE_HIDDEN

            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            binding.overlay.isVisible = true
                        }

                        else -> {
                            binding.overlay.isVisible = false
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }
    }

    private fun setupRecyclerView() {
        playlistsAdapter = AudioplayerPlaylistsAdapter(
            mutableListOf(),
            onPlaylistClick = { playlist ->
                val trackJson = requireArguments().getString(CLICKED_TRACK_CONTENT) ?: ""
                val track = viewModel.fromJson(trackJson, Track::class.java)
                viewModel.addTrackToPlaylist(playlist, track)

            },
            tracksCountDescriptor = { tracksCount -> Useful.itemsText (
                tracksCount,
                getString(R.string.track_items_count_variant1, tracksCount),
                getString(R.string.track_items_count_variant2, tracksCount),
                getString(R.string.track_items_count_variant3, tracksCount)
            )}

        )
        binding.playlistsRecyclerView.adapter = playlistsAdapter
    }

    private fun setText(text: String?, key: TextView?, view: TextView?) {
        view?.text = text
        if (Objects.isNull(text) || text.isNullOrEmpty()) {
            view?.isVisible = false
            key?.isVisible = false
        }
    }

    private var isStarted: Boolean = false

    private fun playbackControl() {
        when (playerState) {
            AudioPlayerViewModel.STATE_PLAYING -> {
                viewModel.pausePlayer()
            }

            AudioPlayerViewModel.STATE_PREPARED, AudioPlayerViewModel.STATE_PAUSED -> {
                viewModel.startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onStop() {
        super.onStop()
        viewModel.pausePlayer()
    }


    companion object {
        const val CLICKED_TRACK_CONTENT = "track"

        fun createArgs(track: String): Bundle =
            bundleOf(CLICKED_TRACK_CONTENT to track)
    }
}