package dom.dima.practicum.playlistmaker.media.ui.fragment.media

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import dom.dima.practicum.playlistmaker.media.ui.fragment.favorites.FavoritesTracksFragment
import dom.dima.practicum.playlistmaker.media.ui.fragment.playlists.dictionary.PlaylistsFragment


class MediaViewPagerAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FavoritesTracksFragment.Companion.newInstance()
            else -> PlaylistsFragment.Companion.newInstance()
        }
    }
}
