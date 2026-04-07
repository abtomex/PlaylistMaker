package dom.dima.practicum.playlistmaker.media.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import dom.dima.practicum.playlistmaker.media.ui.fragment.media.YsDisplayRegular

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PlaylistGridItem(
    playlist: Playlist,
    onClick: () -> Unit
) {
    val tracksCount = playlist.trackIds.size
    val tracksCountText = pluralStringResource(
        id = R.plurals.tracks_count,
        count = tracksCount,
        tracksCount
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        GlideImage(
            model = playlist.cover,
            contentDescription = playlist.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            loading = placeholder(R.drawable.ic_no_image_placeholder_45),
            failure = placeholder(R.drawable.ic_no_image_placeholder_45)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = playlist.title,
            fontSize = 11.sp,
            maxLines = 1,
            color = colorResource(R.color.track_text_color),
            fontFamily = YsDisplayRegular
        )

        Text(
            text = tracksCountText,
            fontSize = 11.sp,
            maxLines = 1,
            color = colorResource(R.color.track_text_artist_color),
            fontFamily = YsDisplayRegular
        )
    }
}