package com.openwheel.regulations.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openwheel.regulations.data.model.RegulationYear
import com.openwheel.regulations.data.repository.RegulationRepository
import com.openwheel.regulations.ui.navigation.NavRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val isLoading: Boolean = true,
    val year: RegulationYear? = null,
    val isBookmarked: Boolean = false
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: RegulationRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val year: Int = savedStateHandle.get<Int>(NavRoutes.Detail.ARG_YEAR)
        ?: Int.MIN_VALUE

    val uiState: StateFlow<DetailUiState> = combine(
        repository.getYear(year),
        repository.isBookmarked(year)
    ) { regulationYear, bookmarked ->
        DetailUiState(
            isLoading = false,
            year = regulationYear,
            isBookmarked = bookmarked
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DetailUiState()
    )

    fun toggleBookmark() {
        viewModelScope.launch { repository.toggleBookmark(year) }
    }
}
