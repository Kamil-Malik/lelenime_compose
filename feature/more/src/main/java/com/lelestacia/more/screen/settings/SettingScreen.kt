package com.lelestacia.more.screen.settings

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lelestacia.more.component.DarkModeSettings
import com.lelestacia.more.component.DisplayStyleSettings
import com.lelestacia.more.component.DynamicColorSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    state: SettingScreenState,
    onEvent: (SettingScreenEvent) -> Unit,
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back Button"
                        )
                    }
                }
            )
        }
    ) { paddingValue ->
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            DisplayStyleSettings(
                isOpened = state.isDisplayStylePreferencesMenuOpened,
                selectedStyle = state.displayStylePreferences,
                onEvent = { onEvent(SettingScreenEvent.UpdateDisplayStylePreferences(it)) },
                changeState = { onEvent(SettingScreenEvent.DisplayStylePreferencesMenuStateChanged) }
            )
            Divider()
            DarkModeSettings(
                isOpened = state.isDarkModePreferencesMenuOpened,
                selectedTheme = state.darkModePreferences,
                onEvent = { onEvent(SettingScreenEvent.UpdateDarkModePreferences(it)) },
                changeState = { onEvent(SettingScreenEvent.DarkModePreferencesMenuStateChanged) }
            )
            if (Build.VERSION.SDK_INT >= 31) {
                Divider()
                DynamicColorSettings(state, onEvent)
            }
        }
    }
}

@Preview
@Composable
fun PreviewSettingScreen() {
    SettingScreen(
        state = SettingScreenState(),
        onEvent = {},
        navController = rememberNavController()
    )
}