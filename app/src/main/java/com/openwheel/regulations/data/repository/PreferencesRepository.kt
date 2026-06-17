package com.openwheel.regulations.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.openwheel.regulations.data.model.AppPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/** Reads and writes user preferences backed by Jetpack DataStore. */
@Singleton
class PreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private object Keys {
        val DARK_THEME = booleanPreferencesKey("dark_theme")
        val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
        val SHOW_ERA_HIGHLIGHTS = booleanPreferencesKey("show_era_highlights")
        val SHOW_STAGGER_ANIMATION = booleanPreferencesKey("show_stagger_animation")
    }

    val preferences: Flow<AppPreferences> = dataStore.data.map { prefs ->
        AppPreferences(
            darkTheme = prefs[Keys.DARK_THEME] ?: true,
            dynamicColor = prefs[Keys.DYNAMIC_COLOR] ?: false,
            showEraHighlights = prefs[Keys.SHOW_ERA_HIGHLIGHTS] ?: true,
            showStaggerAnimation = prefs[Keys.SHOW_STAGGER_ANIMATION] ?: true
        )
    }

    suspend fun setDarkTheme(enabled: Boolean) =
        dataStore.edit { it[Keys.DARK_THEME] = enabled }

    suspend fun setDynamicColor(enabled: Boolean) =
        dataStore.edit { it[Keys.DYNAMIC_COLOR] = enabled }

    suspend fun setShowEraHighlights(enabled: Boolean) =
        dataStore.edit { it[Keys.SHOW_ERA_HIGHLIGHTS] = enabled }

    suspend fun setShowStaggerAnimation(enabled: Boolean) =
        dataStore.edit { it[Keys.SHOW_STAGGER_ANIMATION] = enabled }
}
