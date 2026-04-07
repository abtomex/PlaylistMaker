package dom.dima.practicum.playlistmaker.settings.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dom.dima.practicum.playlistmaker.R

@Preview(showBackground = true)
@Composable
fun SettingsElementPreview() {
    SettingsElement(
        itemText = "Элемент",
        elementResolver = {
            Switch(
                checked = true,
                onCheckedChange = { },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorResource(R.color.switch_thumb_active_color),
                    uncheckedThumbColor = colorResource(R.color.switch_thumb_inactive_color),
                    checkedTrackColor = colorResource(R.color.switch_track_active_color),
                    uncheckedTrackColor = colorResource(R.color.switch_track_inactive_color),
                )
            )
        }, {})
}

@Composable
fun SettingsElement(
    itemText: String,
    elementResolver:@Composable () -> Unit,
    actionClickElement: () -> Unit

) {
    Row(
        modifier = Modifier
            .height(56.dp)
            .padding(horizontal = 16.dp)
            .clickable { actionClickElement() },
        verticalAlignment = Alignment.CenterVertically

    ) {
        Text(
            text = itemText,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f),
            color = colorResource(R.color.text_color)
        )

        elementResolver()

    }
}
