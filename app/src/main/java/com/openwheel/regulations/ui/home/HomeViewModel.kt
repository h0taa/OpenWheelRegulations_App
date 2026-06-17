package com.openwheel.regulations.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openwheel.regulations.data.model.AppPreferences
import com.openwheel.regulations.data.model.RegulationYear
import com.openwheel.regulations.data.repository.PreferencesRepository
import com.openwheel.regulations.data.repository.RegulationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val allYears: List<RegulationYear> = emptyList(),
    val visibleYears: List<RegulationYear> = emptyList(),
    val bookmarkedYears: Set<Int> = emptySet(),
    val preferences: AppPreferences = AppPreferences(),
    val searchQuery: String = "",
    val isSearchActive: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RegulationRepository,
    preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val isSearchActive = MutableStateFlow(false)

    val uiState: StateFlow<HomeUiState> = combine(
        repository.getAllYears(),
        repository.getBookmarkedYears(),
        preferencesRepository.preferences,
        searchQuery,
        isSearchActive
    ) { years, bookmarkedList, prefs, query, searchActive ->
        val bookmarks = bookmarkedList.toSet()
        val visible = if (query.isBlank()) {
            years
        } else {
            val q = query.trim().lowercase()
            years.filter { year ->
                year.year.toString().contains(q) ||
                    year.eraName.lowercase().contains(q) ||
                    year.headline.lowercase().contains(q) ||
                    year.keyChanges.any { it.lowercase().contains(q) }
            }
        }
        HomeUiState(
            isLoading = false,
            allYears = years,
            visibleYears = visible,
            bookmarkedYears = bookmarks,
            preferences = prefs,
            searchQuery = query,
            isSearchActive = searchActive
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )

    init {
        // Deterministic first-frame seeding (idempotent with the Room callback).
        viewModelScope.launch { repository.ensureSeeded() }
    }

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    fun onSearchActiveChange(active: Boolean) {
        isSearchActive.value = active
        if (!active) searchQuery.value = ""
    }

    fun toggleBookmark(year: Int) {
        viewModelScope.launch { repository.toggleBookmark(year) }
    }
}
