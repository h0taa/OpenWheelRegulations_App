package com.openwheel.regulations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openwheel.regulations.data.model.AppPreferences
import com.openwheel.regulations.data.repository.PreferencesRepository
import com.openwheel.regulations.ui.navigation.AppNavGraph
import com.openwheel.regulations.ui.theme.OWRTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        // Show the system splash only briefly; the in-app animated splash takes over.
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val prefs by preferencesRepository.preferences
                .collectAsStateWithLifecycle(initialValue = AppPreferences())

            OWRTheme(
                darkTheme = prefs.darkTheme,
                dynamicColor = prefs.dynamicColor
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavGraph()
                }
            }
        }
    }
}
