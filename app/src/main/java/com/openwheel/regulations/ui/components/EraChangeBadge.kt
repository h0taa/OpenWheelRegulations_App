package com.openwheel.regulations.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.openwheel.regulations.ui.theme.AccentRed
import com.openwheel.regulations.ui.theme.OffWhite

/**
 * Small pill that flags a major-regulation-change season.
 *
 * @param containerColor defaults to the brand accent red; the Admin panel passes gold.
 */
@Composable
fun EraChangeBadge(
    modifier: Modifier = Modifier,
    text: String = "ERA CHANGE",
    containerColor: Color = AccentRed,
    contentColor: Color = OffWhite
) {
    Surface(
        modifier = modifier.semantics { contentDescription = "Major regulation change season" },
        shape = RoundedCornerShape(50),
        color = containerColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color = contentColor
            )
        }
    }
}
