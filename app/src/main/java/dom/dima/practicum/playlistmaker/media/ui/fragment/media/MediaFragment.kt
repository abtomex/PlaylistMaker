package dom.dima.practicum.playlistmaker.media.ui.fragment.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.media.ui.composable.MediaScreen
import dom.dima.practicum.playlistmaker.media.ui.view_model.FavoriteTracksViewModel
import dom.dima.practicum.playlistmaker.media.ui.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

val YsDisplayMedium = FontFamily(
    Font(R.font.ys_display_medium)
)

val YsDisplayRegular = FontFamily(
    Font(R.font.ys_display_regular)
)

class MediaFragment : Fragment() {

    private val favoriteTracksViewModel: FavoriteTracksViewModel by viewModel()
    private val playlistsViewModel: PlaylistsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                MediaScreen(
                    navController = findNavController(),
                    favoriteTracksViewModel = favoriteTracksViewModel,
                    playlistsViewModel = playlistsViewModel
                )
            }
        }
    }
}
