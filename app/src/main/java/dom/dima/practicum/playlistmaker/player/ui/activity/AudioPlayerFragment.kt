package dom.dima.practicum.playlistmaker.player.ui.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.databinding.FragmentAudioplayerBinding
import dom.dima.practicum.playlistmaker.player.ui.state.AudioPlayerState
import dom.dima.practicum.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Objects

@Suppress("DEPRECATION", "INFERRED_TYPE_VARIABLE_INTO_POSSIBLE_EMPTY_INTERSECTION")
class AudioPlayerFragment : Fragment() {

    private var _binding: FragmentAudioplayerBinding? = null
    private val binding get() = _binding!!

    private var playerState = AudioPlayerViewModel.STATE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())

    private val viewModel by viewModel<AudioPlayerViewModel>()

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

        binding.actionBack.setOnClickListener {
            findNavController().popBackStack()
        }


        Glide.with(this)
            .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .fitCenter()
            .placeholder(R.drawable.ic_no_image_placeholder_45)
            .transform(RoundedCorners(dpToPx(8.0f, requireActivity())))
            .into(trackIcon)
        durability.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        setText("00:00", null, binding.progress)
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

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            playerState = state.stateData.playerState
            when (state) {
                is AudioPlayerState.Prepared -> {
                    commonButton.isEnabled = true
                }

                is AudioPlayerState.Completion -> {
                    handler.removeCallbacks(progressRunnable)
                    isStarted = false
                    commonButton.setImageResource(R.drawable.button_play)
                    setText("00:00", null, binding.progress)
                }

                is AudioPlayerState.Start -> {
                    commonButton.setImageResource(R.drawable.button_pause)
                    isStarted = true
                    handler.postDelayed(progressRunnable, DELAY)

                }

                is AudioPlayerState.Pause -> {
                    handler.removeCallbacks(progressRunnable)
                    isStarted = false
                    commonButton.setImageResource(R.drawable.button_play)

                }
            }


        }

    }

    private fun setText(text: String?, key: TextView?, view: TextView?) {
        view?.text = text
        if (Objects.isNull(text) || text.isNullOrEmpty()) {
            view?.isVisible = false
            key?.isVisible = false
        }
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    private var isStarted: Boolean = false


    private val progressRunnable = object : Runnable {
        override fun run() {
            setText(
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(viewModel.currentPosition()), null, binding.progress
            )
            handler.postDelayed(this, DELAY)
        }

    }

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

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(progressRunnable)
    }

    companion object {
        private const val DELAY = 500L
        const val CLICKED_TRACK_CONTENT = "track"

        fun createArgs(track: String): Bundle =
            bundleOf(CLICKED_TRACK_CONTENT to track)
    }
}