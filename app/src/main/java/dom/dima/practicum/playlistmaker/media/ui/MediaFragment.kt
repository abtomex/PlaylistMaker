package dom.dima.practicum.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.databinding.FragmentMediaBinding
import dom.dima.practicum.playlistmaker.media.view_model.MediaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class MediaFragment : Fragment() {

    private var _binding: FragmentMediaBinding? = null
    private val binding get() = _binding!!
    private lateinit var tabsMediator: TabLayoutMediator
    private val viewModel: MediaViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mediaViewPager.adapter = MediaViewPagerAdapter(childFragmentManager, lifecycle)

        tabsMediator = TabLayoutMediator(binding.mediaTabLayout, binding.mediaViewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }

        tabsMediator.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabsMediator.detach()
        _binding = null
    }

}