package dom.dima.practicum.playlistmaker.search.ui.composable

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dom.dima.practicum.playlistmaker.R

@Composable
fun SearchToolbar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.search),
                color = colorResource(R.color.text_color),
                fontSize = 22.sp
            )
        },
        backgroundColor = colorResource(R.color.bkg_window_color_),
        elevation = 0.dp
    )
}
