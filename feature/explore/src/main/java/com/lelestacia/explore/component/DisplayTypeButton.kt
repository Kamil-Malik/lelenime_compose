package com.lelestacia.explore.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.lelestacia.explore.screen.DisplayType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayTypeButton(
    isActive: Boolean,
    isDarkMode: Boolean,
    displayType: DisplayType,
    icon: ImageVector,
    onClicked: (DisplayType) -> Unit
) {
    AssistChip(
        onClick = {
            if (isActive) return@AssistChip
            onClicked(displayType)
        },
        label = {
            Text(
                text = displayType.name
            )
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor =
            if (isActive) {
                MaterialTheme.colorScheme.primary
            } else {
                Color.Transparent
            },
            leadingIconContentColor =
            if (isDarkMode) {
                if (isActive) {
                    Color.Black
                } else {
                    MaterialTheme.colorScheme.primary
                }
            } else {
                if (isActive) {
                    Color.White
                } else {
                    MaterialTheme.colorScheme.primary
                }
            },
            labelColor =
            if (isDarkMode) {
                if (isActive) {
                    Color.Black
                } else {
                    Color.White
                }
            } else {
                if (isActive) {
                    Color.White
                } else {
                    Color.Black
                }
            }
        ),
        border = AssistChipDefaults.assistChipBorder(
            borderColor =
            if (isActive) {
                Color.Transparent
            } else {
                MaterialTheme.colorScheme.outline
            }
        )
    )
}

@Preview(uiMode = UI_MODE_NIGHT_NO or UI_MODE_TYPE_NORMAL)
@Composable
fun PreviewDisplayTypeButtonActive() {
    DisplayTypeButton(
        isActive = true,
        isDarkMode = false,
        displayType = DisplayType.POPULAR,
        icon = Icons.Filled.Favorite,
        onClicked = {}
    )
}

@Preview(uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL)
@Composable
fun PreviewDisplayTypeButtonActiveDarkMode() {
    DisplayTypeButton(
        isActive = true,
        isDarkMode = false,
        displayType = DisplayType.POPULAR,
        icon = Icons.Filled.Favorite,
        onClicked = {}
    )
}

@Preview(uiMode = UI_MODE_NIGHT_NO or UI_MODE_TYPE_NORMAL)
@Composable
fun PreviewDisplayTypeButtonInactive() {
    DisplayTypeButton(
        isActive = false,
        isDarkMode = false,
        displayType = DisplayType.POPULAR,
        icon = Icons.Filled.Favorite,
        onClicked = {}
    )
}

@Preview(uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL)
@Composable
fun PreviewDisplayTypeButtonInactiveDarkMode() {
    DisplayTypeButton(
        isActive = false,
        isDarkMode = false,
        displayType = DisplayType.POPULAR,
        icon = Icons.Filled.Favorite,
        onClicked = {}
    )
}
