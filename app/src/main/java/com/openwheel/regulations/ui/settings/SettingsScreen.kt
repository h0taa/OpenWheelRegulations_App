package com.openwheel.regulations.ui.settings

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openwheel.regulations.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onAboutClick: () -> Unit,
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val prefs by viewModel.preferences.collectAsStateWithLifecycle()
    val supportsDynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            SectionLabel("Appearance")

            SwitchRow(
                icon = Icons.Filled.DarkMode,
                title = "Dark theme",
                subtitle = "Use the dark colour scheme",
                checked = prefs.darkTheme,
                onCheckedChange = viewModel::setDarkTheme
            )

            if (supportsDynamicColor) {
                SwitchRow(
                    icon = Icons.Filled.AutoAwesome,
                    title = "Material You",
                    subtitle = "Tint the app with your system wallpaper colours",
                    checked = prefs.dynamicColor,
                    onCheckedChange = viewModel::setDynamicColor
                )
            }

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            SectionLabel("Behaviour")

            SwitchRow(
                icon = Icons.Filled.Stars,
                title = "Show era highlights",
                subtitle = "Emphasise major regulation-change seasons",
                checked = prefs.showEraHighlights,
                onCheckedChange = viewModel::setShowEraHighlights
            )

            SwitchRow(
                icon = Icons.Filled.Animation,
                title = "Show stagger animation",
                subtitle = "Animate cards as the list appears",
                checked = prefs.showStaggerAnimation,
                onCheckedChange = viewModel::setShowStaggerAnimation
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            SectionLabel("About")

            ListItem(
                headlineContent = { Text("About this app") },
                supportingContent = { Text("Disclaimer, credits and version") },
                leadingContent = {
                    Icon(Icons.Filled.Info, contentDescription = null)
                },
                trailingContent = {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null
                    )
                },
                modifier = Modifier.clickable(onClick = onAboutClick),
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background)
            )

            ListItem(
                headlineContent = { Text("Version") },
                supportingContent = { Text(Constants.APP_VERSION_NAME) },
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background)
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp)
    )
}

@Composable
private fun SwitchRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(subtitle) },
        leadingContent = { Icon(icon, contentDescription = null) },
        trailingContent = {
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        },
        modifier = Modifier.clickable { onCheckedChange(!checked) },
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background)
    )
}
