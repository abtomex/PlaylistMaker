package dom.dima.practicum.playlistmaker.media.ui.fragment.playlists

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import dom.dima.practicum.playlistmaker.media.ui.state.PlaylistStateVM
import dom.dima.practicum.playlistmaker.media.ui.view_model.NewPlaylistViewModel
import dom.dima.practicum.playlistmaker.utils.Useful
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : Fragment() {
    @Volatile
    private var coverUri: Uri? = null

    @Volatile
    private var title: String = ""
    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewPlaylistViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.actionBack.setOnClickListener {
            if (title.trim().isNotEmpty() || coverUri != null) {
                showExitConfirmationDialog()
            } else {
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (title.trim().isNotEmpty() || coverUri != null) {
                        showExitConfirmationDialog()
                    } else {
                        findNavController().popBackStack()
                    }
                }
            }
        )
        binding.createPlaylistButton.setOnClickListener {
            viewModel.createPlaylist(

                binding.newPlaylistTitleInput.text.toString(),
                coverUri,
                binding.newPlaylistDescriptionInput.text?.toString()
            )
        }
        setupTitleText()

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    viewModel.saveImageToPrivateStorage(uri)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        binding.newPlaylistImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        viewModel.getPlaylistState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistStateVM.CoverCreated -> {

                    Glide.with(this)
                        .load(state.uri)
                        .fitCenter()
                        .placeholder(R.drawable.ic_no_image_placeholder_45)
                        .transform(RoundedCorners(Useful.dpToPx(8.0f, requireActivity())))
                        .into(binding.playlistCover)
                    this.coverUri = state.uri

                    binding.addCoverIcon.visibility = View.GONE

                }

                is PlaylistStateVM.Added -> {
                    val playlistName = binding.newPlaylistTitleInput.text?.toString()
                        ?: getString(R.string.no_title)
                    showSuccessToast(playlistName)
                    findNavController().popBackStack()
                }

                is PlaylistStateVM.Error -> {}
            }
        }

    }

    private fun showSuccessToast(playlistName: String) {

        Toast.makeText(
            requireContext(),
            getString(R.string.playlist_created, playlistName),
            Toast.LENGTH_LONG
        ).show()

    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.break_playlist_creation))
            .setMessage(getString(R.string.break_playlist_creation_message))
            .setPositiveButton(getString(R.string.continue_breaking)) { _, _ ->
                findNavController().popBackStack()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun setupTitleText() {
        with(binding.newPlaylistTitleInput) {

            addTextChangedListener(createTextWatcher())
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun createTextWatcher() = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

        override fun afterTextChanged(s: Editable?) {
            title = s.toString().trim()
            if (s.toString().trim().isEmpty()) {
                binding.createPlaylistButton.isEnabled = false
                binding.createPlaylistButton.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray))
                return
            }
            binding.createPlaylistButton.isEnabled = true
            binding.createPlaylistButton.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.blue))

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}