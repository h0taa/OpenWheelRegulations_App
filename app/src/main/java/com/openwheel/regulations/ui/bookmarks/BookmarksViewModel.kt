package com.openwheel.regulations.ui.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openwheel.regulations.data.model.RegulationYear
import com.openwheel.regulations.data.repository.PreferencesRepository
import com.openwheel.regulations.data.repository.RegulationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/** UI state for the Bookmarks screen. */
data class BookmarksUiState(
    val isLoading: Boolean = true,
    val bookmarkedYears: List<RegulationYear> = emptyList(),
    val showEraHighlights: Boolean = true
)

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val repository: RegulationRepository,
    preferencesRepository: PreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<BookmarksUiState> = combine(
        repository.getAllYears(),
        repository.getBookmarkedYears(),
        preferencesRepository.preferences
    ) { allYears, bookmarkedIds, prefs ->
        val ids = bookmarkedIds.toSet()
        // Preserve the existing newest-first ordering of the full list.
        val bookmarked = allYears.filter { it.year in ids }
        BookmarksUiState(
            isLoading = false,
            bookmarkedYears = bookmarked,
            showEraHighlights = prefs.showEraHighlights
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BookmarksUiState()
    )

    fun removeBookmark(year: Int) {
        viewModelScope.launch { repository.toggleBookmark(year) }
    }
}
