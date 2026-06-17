package com.openwheel.regulations.ui.bookmarks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openwheel.regulations.ui.components.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(
    onYearClick: (Int) -> Unit,
    onBack: () -> Unit,
    viewModel: BookmarksViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bookmarks") },
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
        if (state.bookmarkedYears.isEmpty() && !state.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                EmptyState(
                    title = "No bookmarks yet",
                    message = "Tap the star on any season to save it here for quick access."
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = innerPadding.calculateTopPadding() + 12.dp,
                    bottom = innerPadding.calculateBottomPadding() + 24.dp
                )
            ) {
                items(
                    items = state.bookmarkedYears,
                    key = { it.year }
                ) { year ->
                    BookmarkYearCard(
                        year = year,
                        showHighlight = state.showEraHighlights,
                        onClick = { onYearClick(year.year) },
                        onRemove = { viewModel.removeBookmark(year.year) },
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
            }
        }
    }
}
