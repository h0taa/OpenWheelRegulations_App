package com.openwheel.regulations.ui.detail

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.openwheel.regulations.data.model.RegulationYear
import com.openwheel.regulations.ui.components.BookmarkButton
import com.openwheel.regulations.ui.components.EraChangeBadge
import com.openwheel.regulations.ui.theme.AccentRed

@OptIn(
    ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun DetailScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    // Slide the comparison cars in once the comparison item scrolls into view.
    val comparisonVisible by remember {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo.any { it.key == "comparison" }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    BookmarkButton(
                        isBookmarked = state.isBookmarked,
                        onToggle = viewModel::toggleBookmark
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val year = state.year
            when {
                state.isLoading -> CircularProgressIndicator(
                    color = AccentRed,
                    modifier = Modifier.align(Alignment.Center)
                )

                year == null -> Text(
                    text = "Season not found.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.Center)
                )

                else -> DetailContent(
                    year = year,
                    listState = listState,
                    comparisonVisible = comparisonVisible,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalFoundationApi::class)
@Composable
private fun DetailContent(
    year: RegulationYear,
    listState: androidx.compose.foundation.lazy.LazyListState,
    comparisonVisible: Boolean,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Sticky hero
        stickyHeader {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = 12.dp)
            ) {
                if (year.isMajorChange) {
                    EraChangeBadge(modifier = Modifier.padding(bottom = 8.dp))
                }
                with(sharedTransitionScope) {
                    Text(
                        text = year.year.toString(),
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.displayLarge,
                        color = AccentRed,
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState(key = "year-${year.year}"),
                            animatedVisibilityScope = animatedContentScope
                        )
                    )
                }
                Text(
                    text = year.eraName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = year.headline,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }

        item(key = "keyChanges") {
            ExpandableRegulationCard(
                title = "Key Changes",
                body = year.keyChanges.joinToString("\n\n") { "•  $it" },
                initiallyExpanded = true
            )
        }
        item(key = "dimensions") {
            ExpandableRegulationCard("Car Dimensions & Weight", year.carDimensions)
        }
        item(key = "powerUnit") {
            ExpandableRegulationCard("Power Unit / Engine", year.powerUnit)
        }
        item(key = "aero") {
            ExpandableRegulationCard("Aerodynamic Rules", year.aerodynamicRules)
        }
        item(key = "tyres") {
            ExpandableRegulationCard("Tyre Regulations", year.tyreRegulations)
        }
        item(key = "impact") {
            ExpandableRegulationCard("Notable Season Impact", year.notableImpact)
        }

        // Visual comparison — major years only, always last.
        if (year.isMajorChange && year.visualComponents.isNotEmpty()) {
            item(key = "comparison") {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text(
                        text = "Visual Comparison",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    CarComparisonCanvas(
                        changes = year.visualComponents,
                        animateIn = comparisonVisible
                    )
                }
            }
        }
    }
}
