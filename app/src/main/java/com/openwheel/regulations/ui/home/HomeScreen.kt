package com.openwheel.regulations.ui.home

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.openwheel.regulations.data.model.RegulationYear
import com.openwheel.regulations.ui.components.FunFactsBottomSheet
import com.openwheel.regulations.ui.theme.AccentRed
import com.openwheel.regulations.util.Constants
import kotlinx.coroutines.delay

/** How many leading rows get the one-time entrance stagger on first load. */
private const val INITIAL_STAGGER_COUNT = 7

@OptIn(
    ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun HomeScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onYearClick: (Int) -> Unit,
    onBookmarksClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    var factsYear by remember { mutableStateOf<RegulationYear?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "OWR",
                        style = MaterialTheme.typography.titleLarge,
                        color = AccentRed,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.onSearchActiveChange(!state.isSearchActive) }) {
                        Icon(Icons.Outlined.Search, contentDescription = "Search seasons")
                    }
                    IconButton(onClick = onBookmarksClick) {
                        Icon(Icons.Outlined.BookmarkBorder, contentDescription = "Bookmarks")
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Outlined.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = AccentRed
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = AccentRed,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                HomeContent(
                    state = state,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope,
                    onQueryChange = viewModel::onSearchQueryChange,
                    onCloseSearch = { viewModel.onSearchActiveChange(false) },
                    onYearClick = onYearClick,
                    onLongPress = { factsYear = it },
                    onBookmarkToggle = viewModel::toggleBookmark
                )
            }
        }
    }

    factsYear?.let { y ->
        FunFactsBottomSheet(
            year = y.year,
            facts = y.funFacts,
            onDismiss = { factsYear = null }
        )
    }
}

@OptIn(
    ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
private fun HomeContent(
    state: HomeUiState,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onQueryChange: (String) -> Unit,
    onCloseSearch: () -> Unit,
    onYearClick: (Int) -> Unit,
    onLongPress: (RegulationYear) -> Unit,
    onBookmarkToggle: (Int) -> Unit
) {
    // The entrance stagger plays once, only for the first screenful, and never again as
    // rows recycle during scrolling — otherwise deep rows pop in late and the scroll stutters.
    val staggerEnabled = state.preferences.showStaggerAnimation && !state.isSearchActive
    val firstYear = state.visibleYears.firstOrNull()?.year ?: 0
    var staggerDone by remember(staggerEnabled, firstYear) { mutableStateOf(!staggerEnabled) }
    LaunchedEffect(staggerEnabled, firstYear) {
        if (staggerEnabled && firstYear != 0) {
            delay(INITIAL_STAGGER_COUNT * Constants.STAGGER_ITEM_DELAY_MS + 300L)
            staggerDone = true
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 24.dp)
    ) {
        stickyHeader {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Seasons 2000 – 2026",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        item {
            AnimatedVisibility(
                visible = state.isSearchActive,
                enter = expandVertically(tween(220)) + fadeIn(tween(220)),
                exit = shrinkVertically(tween(180)) + fadeOut(tween(180))
            ) {
                TextField(
                    value = state.searchQuery,
                    onValueChange = onQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    placeholder = { Text("Search year, era or change…") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = onCloseSearch) {
                            Icon(Icons.Filled.Close, contentDescription = "Close search")
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
                )
            }
        }

        if (state.visibleYears.isEmpty()) {
            item {
                Text(
                    text = "No seasons match \"${state.searchQuery}\".",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 32.dp)
                )
            }
        }

        itemsIndexed(
            items = state.visibleYears,
            key = { _, year -> year.year }
        ) { index, year ->
            StaggeredYearItem(
                index = index,
                animate = staggerEnabled && !staggerDone && index < INITIAL_STAGGER_COUNT
            ) {
                YearCard(
                    year = year,
                    isBookmarked = state.bookmarkedYears.contains(year.year),
                    showHighlight = state.preferences.showEraHighlights,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope,
                    onClick = { onYearClick(year.year) },
                    onLongPress = { onLongPress(year) },
                    onBookmarkToggle = { onBookmarkToggle(year.year) },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

/**
 * Wraps a list item in a one-shot fade + slide-in-from-right entrance, staggered by
 * [index]. When [animate] is false the content is shown immediately.
 */
@Composable
private fun StaggeredYearItem(
    index: Int,
    animate: Boolean,
    content: @Composable () -> Unit
) {
    if (!animate) {
        content()
        return
    }
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(index * Constants.STAGGER_ITEM_DELAY_MS)
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(280)) + slideInHorizontally(tween(280)) { full -> full / 3 }
    ) {
        content()
    }
}
