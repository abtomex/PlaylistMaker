package dom.dima.practicum.playlistmaker.settings.ui.composable

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.media.ui.fragment.media.YsDisplayMedium
import dom.dima.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel,
    context: Context
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.bkg_window_color_))
            .statusBarsPadding()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.settings),
                    color = colorResource(R.color.text_color),
                    fontSize = 22.sp,
                    fontFamily = YsDisplayMedium
                )
            },
            backgroundColor = colorResource(R.color.bkg_window_color_),
            elevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        val themeSwitcher = @Composable {
            Switch(
                checked = viewModel.isDarkThemeOn(),
                onCheckedChange = { checked -> viewModel.changeTheme(checked) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorResource(R.color.switch_thumb_active_color),
                    uncheckedThumbColor = colorResource(R.color.switch_thumb_inactive_color),
                    checkedTrackColor = colorResource(R.color.switch_track_active_color),
                    uncheckedTrackColor = colorResource(R.color.switch_track_inactive_color),
                )
            ) }

        SettingsElement(
            itemText = stringResource(R.string.dark_theme),
            elementResolver = themeSwitcher,
            actionClickElement = {}
        )
        SettingsElement(
            itemText = stringResource(R.string.share_app),
            elementResolver = {
                Icon(
                    painter = painterResource(R.drawable.share),
                    contentDescription = stringResource(R.string.share),
                    tint = colorResource(R.color.icons_color_settings)
                )
            },
            actionClickElement = {
                viewModel.doShare(context.getString(R.string.android_course_url))
            }
        )
        SettingsElement(
            itemText = stringResource(R.string.support),
            elementResolver = {
                Icon(
                    painter = painterResource(R.drawable.support),
                    contentDescription = stringResource(R.string.support),
                    tint = colorResource(R.color.icons_color_settings)
                )
            },
            actionClickElement = {
                viewModel.doWrightTechSupport(
                    arrayOf(context.getString(R.string.my_email)),
                    context.getString(R.string.email_subject),
                    context.getString(R.string.email_text)
                )
            }
        )
        SettingsElement(
            itemText = stringResource(R.string.agreement),
            elementResolver = {
                Icon(
                    painter = painterResource(R.drawable.next),
                    contentDescription = stringResource(R.string.agreement),
                    tint = colorResource(R.color.icons_color_settings)
                )
            },
            actionClickElement = {
                navController.navigate(R.id.action_settingsFragment_to_agreementFragment)
            }
        )

    }
}
