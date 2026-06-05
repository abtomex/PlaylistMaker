package dom.dima.practicum.playlistmaker.media.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.media.ui.fragment.media.YsDisplayRegular
import dom.dima.practicum.playlistmaker.media.ui.view_model.FavoriteTracksViewModel
import dom.dima.practicum.playlistmaker.player.ui.activity.AudioPlayerFragment
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import dom.dima.practicum.playlistmaker.utils.Useful

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FavoriteTrackItem(
    track: Track,
    viewModel: FavoriteTracksViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (viewModel.clickIsAllowed) {
                    viewModel.clickIsAllowed = false
                    viewModel.clickDebounce {
                        navController.navigate(
                            R.id.action_mediaFragment_to_audioPlayerFragment,
                            AudioPlayerFragment.createArgs(viewModel.gson().toJson(track))
                        )
                    }
                }
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            model = track.artworkUrl100,
            contentDescription = track.trackName,
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop,
            loading = placeholder(R.drawable.ic_no_image_placeholder_45),
            failure = placeholder(R.drawable.ic_no_image_placeholder_45)
        ) {
            it.transform(RoundedCorners(Useful.dpToPx(2f, context)))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = track.trackName,
                fontSize = 16.sp,
                maxLines = 1,
                color = colorResource(R.color.track_text_color),
                fontFamily = YsDisplayRegular
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = track.artistName ?: "",
                    fontSize = 14.sp,
                    color = colorResource(R.color.track_text_artist_color),
                    maxLines = 1,
                    fontFamily = YsDisplayRegular
                )

                Text(
                    text = "•",
                    fontSize = 14.sp,
                    color = colorResource(R.color.track_text_artist_color),
                    fontFamily = YsDisplayRegular
                )

                Text(
                    text = track.trackTimeStr,
                    fontSize = 14.sp,
                    color = colorResource(R.color.track_text_artist_color),
                    fontFamily = YsDisplayRegular
                )
            }
        }

        Icon(
            painter = painterResource(R.drawable.ic_fwd_arrow_14),
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.width(8.dp).height(16.dp)
        )
    }

}
