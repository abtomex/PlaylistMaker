package dom.dima.practicum.playlistmaker.media.ui

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import dom.dima.practicum.playlistmaker.AbstractButtonBackActivity
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.databinding.ActivityMediaBinding
import dom.dima.practicum.playlistmaker.media.view_model.MediaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaActivity : AbstractButtonBackActivity() {

    private lateinit var binding: ActivityMediaBinding
    private lateinit var tabMediator: TabLayoutMediator
    private val viewModel: MediaViewModel by viewModel()

    override fun buttonBackId(): Int {
        return R.id.media
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mediaViewPager.adapter = MediaViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.mediaTabLayout, binding.mediaViewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }

        tabMediator.attach()

    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }


}
