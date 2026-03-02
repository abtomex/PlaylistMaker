package dom.dima.practicum.playlistmaker.media.ui.fragment.playlists

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.databinding.FragmentPlaylistEditorBinding
import dom.dima.practicum.playlistmaker.media.ui.fragment.playlists.playlist_screen.PlaylistScreenFragment.Companion.CLICKED_PLAYLIST_ID
import dom.dima.practicum.playlistmaker.media.ui.state.PlaylistStateVM
import dom.dima.practicum.playlistmaker.media.ui.view_model.PlaylistEditorViewModel
import dom.dima.practicum.playlistmaker.utils.Useful
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistEditorFragment : Fragment() {
    @Volatile
    private var coverUri: Uri? = null

    @Volatile
    private var title: String = ""
    private var _binding: FragmentPlaylistEditorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistEditorViewModel by viewModel()

    private var galleryIsGranted = false

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                galleryIsGranted = true
            } else {
                galleryIsGranted = false
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.data = Uri.fromParts("package", requireContext().packageName, null)
                startActivity(intent)
            }

        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val playlistId = requireArguments().getInt(CLICKED_PLAYLIST_ID)
        when (playlistId) {
             NEW_PLAYLIST_MARKER -> {
                binding.editorTitle.text = getString(R.string.new_playlist)
                binding.playlistButton.text = getString(R.string.create)
            }
            else -> {
                binding.editorTitle.text = getString(R.string.edit_playlist)
                binding.playlistButton.text = getString(R.string.save)
                viewModel.loadData(playlistId)
            }
        }


        binding.actionBack.setOnClickListener {
            if (title.trim().isNotEmpty() || coverUri != null) {
                showExitConfirmationDialog(playlistId)
            } else {
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (title.trim().isNotEmpty() || coverUri != null) {
                        showExitConfirmationDialog(playlistId)
                    } else {
                        findNavController().popBackStack()
                    }
                }
            }
        )
        binding.playlistButton.setOnClickListener {
            if (playlistId == NEW_PLAYLIST_MARKER ) {
                viewModel.createPlaylist(

                    binding.playlistEditorTitleInput.text.toString(),
                    coverUri,
                    binding.playlistEditorDescriptionInput.text?.toString()
                )
            } else {
                viewModel.updatePlaylist(
                    playlistId,
                    binding.playlistEditorTitleInput.text.toString(),
                    coverUri,
                    binding.playlistEditorDescriptionInput.text?.toString()
                )

            }
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

        binding.playlistCover.setOnClickListener {
            if (checkPermission()) {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
        viewModel.getPlaylistState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistStateVM.LoadData -> {
                    val model = state.playlist
                    binding.playlistEditorTitleInput.setText(model.title)
                    binding.playlistEditorDescriptionInput.setText(model.description)
                    this.coverUri = model.cover

                    Glide.with(this)
                        .load(this.coverUri)
                        .fitCenter()
                        .placeholder(R.drawable.ic_no_image_placeholder_45)
                        .transform(RoundedCorners(Useful.dpToPx(8.0f, requireActivity())))
                        .into(binding.playlistCover)
                    binding.addCoverIcon.visibility = View.GONE

                }
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
                    val playlistName = binding.playlistEditorTitleInput.text?.toString()
                        ?: getString(R.string.no_title)
                    showSuccessToast(playlistName)
                    findNavController().popBackStack()
                }

                is PlaylistStateVM.Error -> {}
                is PlaylistStateVM.Updated -> {
                    findNavController().popBackStack()
                }
            }
        }

    }
    private fun checkPermission(): Boolean {
        galleryIsGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        return galleryIsGranted
    }
    private fun showSuccessToast(playlistName: String) {

        Toast.makeText(
            requireContext(),
            getString(R.string.playlist_created, playlistName),
            Toast.LENGTH_LONG
        ).show()

    }

    private fun showExitConfirmationDialog(playlistId: Int) {
        if (playlistId != NEW_PLAYLIST_MARKER) {
            findNavController().popBackStack()
            return
        }
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
        with(binding.playlistEditorTitleInput) {

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
                binding.playlistButton.isEnabled = false
                binding.playlistButton.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray))
                return
            }
            binding.playlistButton.isEnabled = true
            binding.playlistButton.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.blue))

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val PLAYLIST_ID = "playlistId"
        const val NEW_PLAYLIST_MARKER = -1
        fun createArgs(playlistId: Int): Bundle =
            bundleOf(PLAYLIST_ID to playlistId)
    }

}