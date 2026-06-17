package com.openwheel.regulations.ui.home

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openwheel.regulations.data.model.RegulationYear
import com.openwheel.regulations.ui.components.BookmarkButton
import com.openwheel.regulations.ui.components.EraChangeBadge
import com.openwheel.regulations.ui.theme.AccentRed

/**
 * A single season card on the Home list.
 *
 * Major-regulation-change years (when [RegulationYear.isMajorChange] is true *and* the
 * user has era-highlights enabled) get a clean, distinctive treatment: a solid red
 * accent bar down the leading edge, a faint warm tint, a subtle red border, and an
 * "ERA CHANGE" badge — enough to stand out without the heavy glow it used to have.
 *
 * The big year number is registered as a shared element so it can morph into the Detail
 * hero.
 */
@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalFoundationApi::class)
@Composable
fun YearCard(
    year: RegulationYear,
    isBookmarked: Boolean,
    showHighlight: Boolean,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
    onBookmarkToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val highlighted = year.isMajorChange && showHighlight
    val shape = RoundedCornerShape(20.dp)
    val surface = MaterialTheme.colorScheme.surface

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .combinedClickable(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClick()
                },
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onLongPress()
                }
            ),
        shape = shape,
        colors = CardDefaults.cardColors(
            // A barely-there warm tint on era cards (theme-safe via lerp); plain surface otherwise.
            containerColor = if (highlighted) lerp(surface, AccentRed, 0.05f) else surface
        ),
        border = if (highlighted) BorderStroke(1.5.dp, AccentRed.copy(alpha = 0.45f)) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = if (highlighted) 4.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 4.dp),
            verticalAlignment = Alignment.Top
        ) {
            if (highlighted) {
                // Leading accent bar; the card's rounded clip softens its corners.
                Box(
                    modifier = Modifier
                        .width(6.dp)
                        .fillMaxHeight()
                        .background(AccentRed)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        start = if (highlighted) 16.dp else 18.dp,
                        top = 16.dp,
                        bottom = 16.dp,
                        end = 6.dp
                    )
            ) {
                if (highlighted) {
                    EraChangeBadge(modifier = Modifier.padding(bottom = 8.dp))
                }

                with(sharedTransitionScope) {
                    Text(
                        text = year.year.toString(),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.displayMedium,
                        color = AccentRed,
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState(key = "year-${year.year}"),
                            animatedVisibilityScope = animatedContentScope
                        )
                    )
                }

                Text(
                    text = year.eraName,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    text = year.headline,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    modifier = Modifier.padding(top = 8.dp)
                )

                if (highlighted) {
                    Text(
                        text = "Tap to see the visual comparison →",
                        style = MaterialTheme.typography.labelMedium,
                        color = AccentRed,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }

            BookmarkButton(
                isBookmarked = isBookmarked,
                onToggle = onBookmarkToggle
            )
        }
    }
}
