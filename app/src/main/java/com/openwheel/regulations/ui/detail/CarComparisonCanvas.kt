package com.openwheel.regulations.ui.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openwheel.regulations.R
import com.openwheel.regulations.data.model.VisualChange
import com.openwheel.regulations.ui.theme.AccentRed
import com.openwheel.regulations.ui.theme.PreviousEraGrey
import kotlinx.coroutines.delay

/** Aspect ratio (width / height) of the bundled comparison car image. */
private const val CAR_ASPECT = 4.745f

/** Fractional anchor (x right, y down) of each car component within the car image. */
private fun anchorFor(component: String): Offset = when (component) {
    "frontWing" -> Offset(0.085f, 0.85f)
    "rearWing" -> Offset(0.955f, 0.33f)
    "floor" -> Offset(0.52f, 0.90f)
    "engine" -> Offset(0.57f, 0.18f)
    "tyre" -> Offset(0.22f, 0.66f)
    "body" -> Offset(0.45f, 0.39f)
    else -> Offset(0.5f, 0.5f)
}

/**
 * Interactive era-comparison: the new-era car (a single image, no liveries or sponsor
 * marks) sits on a stage while a moving spotlight focuses each regulation change in turn.
 * A leader line points from the highlighted part to a callout whose Before -> After values
 * animate in. A play/pause auto-tour cycles the changes; the chips let the user pick one.
 */
@Composable
fun CarComparisonCanvas(
    changes: List<VisualChange>,
    animateIn: Boolean,
    modifier: Modifier = Modifier
) {
    if (changes.isEmpty()) return

    var activeIndex by remember { mutableIntStateOf(0) }
    var playing by remember { mutableStateOf(true) }
    val safeIndex = activeIndex.coerceIn(0, changes.lastIndex)
    val active = changes[safeIndex]

    val enter by animateFloatAsState(
        targetValue = if (animateIn) 1f else 0f,
        animationSpec = tween(620),
        label = "enter"
    )

    // Auto-tour: advance through the changes while playing.
    LaunchedEffect(playing, changes.size) {
        if (!playing) return@LaunchedEffect
        while (true) {
            delay(2600)
            activeIndex = (activeIndex + 1) % changes.size
        }
    }

    val target = anchorFor(active.component)
    val ax by animateFloatAsState(target.x, tween(720, easing = androidx.compose.animation.core.FastOutSlowInEasing), label = "ax")
    val ay by animateFloatAsState(target.y, tween(720, easing = androidx.compose.animation.core.FastOutSlowInEasing), label = "ay")
    val pulse by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1300), RepeatMode.Reverse),
        label = "pulseV"
    )

    val accent = AccentRed
    val grey = PreviousEraGrey
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val surface = MaterialTheme.colorScheme.surface
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant

    Column(modifier = modifier.fillMaxWidth()) {

        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val calloutH = 128.dp
            val carH = maxWidth / CAR_ASPECT
            val stageH = carH + calloutH

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(stageH)
            ) {
                // Stage backdrop panel (so the dark car separates from the page on any theme).
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(top = 6.dp, bottom = 6.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.verticalGradient(
                                0f to surfaceVariant.copy(alpha = 0.55f),
                                1f to surfaceVariant.copy(alpha = 0.16f)
                            )
                        )
                )

                // The car, anchored to the bottom and spanning the full width.
                Image(
                    painter = painterResource(id = R.drawable.car_compare),
                    contentDescription = "Open-wheel car",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(carH)
                        .align(Alignment.BottomCenter)
                        .graphicsLayer {
                            alpha = 0.2f + 0.8f * enter
                            val s = 0.97f + 0.03f * enter
                            scaleX = s; scaleY = s
                        }
                )

                // Spotlight + markers + leader line.
                Canvas(modifier = Modifier.matchParentSize()) {
                    val carHpx = size.width / CAR_ASPECT
                    val carTop = size.height - carHpx
                    val cx = ax.coerceIn(0.04f, 0.96f) * size.width
                    val cy = carTop + ay * carHpx
                    val spotR = size.width * 0.22f * (0.96f + 0.04f * pulse)

                    // Dim the car except a soft circle around the focused part.
                    drawRect(
                        brush = Brush.radialGradient(
                            0.0f to Color.Transparent,
                            0.46f to Color.Transparent,
                            1.0f to surfaceVariant.copy(alpha = 0.86f * enter),
                            center = Offset(cx, cy),
                            radius = spotR
                        ),
                        topLeft = Offset(0f, carTop),
                        size = Size(size.width, carHpx)
                    )

                    // Faint markers for the other changes.
                    changes.forEachIndexed { i, c ->
                        if (i != safeIndex) {
                            val f = anchorFor(c.component)
                            drawCircle(
                                color = onSurfaceVariant.copy(alpha = 0.45f * enter),
                                radius = 3.5.dp.toPx(),
                                center = Offset(f.x * size.width, carTop + f.y * carHpx)
                            )
                        }
                    }

                    // Active marker: dot + pulsing ring.
                    drawCircle(accent.copy(alpha = enter), 4.5.dp.toPx(), Offset(cx, cy))
                    drawCircle(
                        color = accent.copy(alpha = (0.55f - 0.32f * pulse) * enter),
                        radius = 13.dp.toPx() * (1f + 0.2f * pulse),
                        center = Offset(cx, cy),
                        style = Stroke(width = 2.dp.toPx())
                    )

                    // Leader line up to the callout (which sits just above carTop).
                    val topX = cx.coerceIn(18.dp.toPx(), size.width - 18.dp.toPx())
                    drawLine(
                        color = accent.copy(alpha = enter),
                        start = Offset(cx, cy - 5.dp.toPx()),
                        end = Offset(topX, carTop),
                        strokeWidth = 1.6.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                    drawCircle(accent.copy(alpha = enter), 3.dp.toPx(), Offset(topX, carTop))
                }

                // Callout card (top area).
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Surface(
                        color = surface.copy(alpha = 0.95f),
                        shape = RoundedCornerShape(12.dp),
                        tonalElevation = 3.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer { alpha = enter }
                    ) {
                        AnimatedContent(
                            targetState = safeIndex,
                            transitionSpec = {
                                (fadeIn(tween(260)) + slideInHorizontally(tween(260)) { w: Int -> w / 6 }) togetherWith
                                    (fadeOut(tween(170)) + slideOutHorizontally(tween(170)) { w: Int -> -w / 6 })
                            },
                            label = "callout"
                        ) { idx ->
                            val c = changes[idx.coerceIn(0, changes.lastIndex)]
                            Column(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Text(
                                    text = c.category.uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = accent,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = c.label,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = onSurface,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1
                                )
                                Spacer(Modifier.height(1.dp))
                                BeforeAfterRow("BEFORE", c.previousValue, grey, onSurfaceVariant)
                                BeforeAfterRow("AFTER", c.newValue, accent, onSurface)
                            }
                        }
                    }
                }
            }
        }

        // Controls: play/pause + progress dots + counter.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledTonalButton(onClick = { playing = !playing }) {
                Icon(
                    imageVector = if (playing) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(if (playing) "Pause" else "Play tour")
            }
            Spacer(Modifier.width(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                changes.forEachIndexed { i, _ ->
                    Box(
                        modifier = Modifier
                            .size(if (i == safeIndex) 9.dp else 7.dp)
                            .clip(CircleShape)
                            .background(if (i == safeIndex) accent else onSurfaceVariant.copy(alpha = 0.4f))
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            Text(
                text = "${safeIndex + 1}/${changes.size}",
                style = MaterialTheme.typography.labelMedium,
                color = onSurfaceVariant
            )
        }

        // Chips to jump to a specific change.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            changes.forEachIndexed { index, change ->
                FilterChip(
                    selected = index == safeIndex,
                    onClick = { activeIndex = index; playing = false },
                    label = { Text(change.label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AccentRed,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    }
}

@Composable
private fun BeforeAfterRow(tag: String, value: String, tagColor: Color, valueColor: Color) {
    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = tag,
            style = MaterialTheme.typography.labelSmall,
            color = tagColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(48.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = valueColor,
            maxLines = 2
        )
    }
}
