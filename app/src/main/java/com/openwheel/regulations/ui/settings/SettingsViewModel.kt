package com.openwheel.regulations.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openwheel.regulations.data.model.AppPreferences
import com.openwheel.regulations.data.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    val preferences: StateFlow<AppPreferences> = preferencesRepository.preferences
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AppPreferences()
        )

    fun setDarkTheme(enabled: Boolean) =
        viewModelScope.launch { preferencesRepository.setDarkTheme(enabled) }

    fun setDynamicColor(enabled: Boolean) =
        viewModelScope.launch { preferencesRepository.setDynamicColor(enabled) }

    fun setShowEraHighlights(enabled: Boolean) =
        viewModelScope.launch { preferencesRepository.setShowEraHighlights(enabled) }

    fun setShowStaggerAnimation(enabled: Boolean) =
        viewModelScope.launch { preferencesRepository.setShowStaggerAnimation(enabled) }
}
