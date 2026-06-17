package com.openwheel.regulations.ui.bookmarks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openwheel.regulations.data.model.RegulationYear
import com.openwheel.regulations.ui.components.BookmarkButton
import com.openwheel.regulations.ui.components.EraChangeBadge
import com.openwheel.regulations.ui.theme.AccentRed

/**
 * Bookmarks-list card. Visually matches [com.openwheel.regulations.ui.home.YearCard]
 * (clean era treatment: accent bar + faint tint + subtle border) but without the
 * shared-element transition, since Bookmarks is not part of the Home -> Detail pair.
 */
@Composable
fun BookmarkYearCard(
    year: RegulationYear,
    showHighlight: Boolean,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    val highlighted = year.isMajorChange && showHighlight
    val shape = RoundedCornerShape(20.dp)
    val surface = MaterialTheme.colorScheme.surface

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(onClick = onClick),
        shape = shape,
        colors = CardDefaults.cardColors(
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
                Text(
                    text = year.year.toString(),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.displayMedium,
                    color = AccentRed
                )
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
            }

            BookmarkButton(
                isBookmarked = true,
                onToggle = onRemove
            )
        }
    }
}
