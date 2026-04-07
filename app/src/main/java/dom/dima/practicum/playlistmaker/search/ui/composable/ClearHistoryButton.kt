package dom.dima.practicum.playlistmaker.search.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dom.dima.practicum.playlistmaker.R

@Preview
@Composable
fun PrevClearHistoryButton() {
    ClearHistoryButton(Modifier
        .fillMaxWidth()
    ) {}
}
@Composable
fun ClearHistoryButton(modifier: Modifier, onClearHistory: () -> Unit) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        Button(
            onClick = onClearHistory,
            modifier = Modifier
                .padding(top = 4.dp)
            ,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(R.color.btn_activity_search_color),
                contentColor = colorResource(R.color.btn_reload_text)
            ),
            shape = RoundedCornerShape(54.dp)
        ) {
            Text(stringResource(R.string.clear_history))
        }
    }

}