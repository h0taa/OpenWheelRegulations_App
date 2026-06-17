package com.openwheel.regulations.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.openwheel.regulations.ui.theme.AccentRed

/**
 * A star button that toggles the bookmarked state, with a subtle scale "pop" and a
 * medium haptic tick on every toggle.
 */
@Composable
fun BookmarkButton(
    isBookmarked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val scale by animateFloatAsState(
        targetValue = if (isBookmarked) 1.12f else 1f,
        label = "bookmarkScale"
    )

    IconButton(
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            onToggle()
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isBookmarked) Icons.Filled.Star else Icons.Outlined.StarBorder,
            contentDescription = if (isBookmarked) "Remove bookmark" else "Add bookmark",
            tint = if (isBookmarked) AccentRed else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.scale(scale)
        )
    }
}
