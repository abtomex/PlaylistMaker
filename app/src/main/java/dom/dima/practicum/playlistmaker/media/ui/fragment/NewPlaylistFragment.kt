package dom.dima.practicum.playlistmaker.media.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dom.dima.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import dom.dima.practicum.playlistmaker.media.ui.view_model.NewPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : Fragment(){
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
            findNavController().popBackStack()
        }
        binding.createPlaylistButton.setOnClickListener {
            viewModel.createPlaylist(
                binding.newPlaylistTitleInput.text?.toString(),
                binding.newPlaylistDescriptionInput.text?.toString()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = NewPlaylistFragment()
    }

}