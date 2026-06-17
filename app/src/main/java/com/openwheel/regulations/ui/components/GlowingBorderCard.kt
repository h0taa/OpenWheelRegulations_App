package com.openwheel.regulations.ui.components

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Draws a soft outer glow behind content — used to make major-regulation-change
 * year cards stand out. Implemented with a native [BlurMaskFilter] in OUTER mode so
 * the blur sits *outside* the rounded rectangle, exactly as the spec requires.
 *
 * @param glowColor the glow tint (defaults to the brand accent red).
 * @param cornerRadius corner radius of the card the glow hugs.
 * @param glowRadius blur radius of the glow.
 */
fun Modifier.glowingBorder(
    glowColor: Color,
    cornerRadius: Dp = 20.dp,
    glowRadius: Dp = 18.dp,
    alpha: Float = 0.55f
): Modifier = this.drawBehind {
    val radiusPx = cornerRadius.toPx()
    val blurPx = glowRadius.toPx()

    drawIntoCanvas { canvas ->
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = Color.Transparent.toArgb()
        frameworkPaint.setShadowLayer(
            blurPx,
            0f,
            0f,
            glowColor.copy(alpha = alpha).toArgb()
        )
        // BlurMaskFilter OUTER keeps the blur strictly outside the shape edge.
        frameworkPaint.maskFilter = BlurMaskFilter(blurPx, BlurMaskFilter.Blur.OUTER)
        frameworkPaint.color = glowColor.copy(alpha = alpha).toArgb()

        canvas.drawRoundRect(
            left = 0f,
            top = 0f,
            right = size.width,
            bottom = size.height,
            radiusX = radiusPx,
            radiusY = radiusPx,
            paint = paint
        )
    }
}

/**
 * Convenience wrapper that places [content] inside a [Box] carrying the glow.
 * Most callers use the [glowingBorder] modifier directly on their card surface,
 * but this is handy for standalone use.
 */
@Composable
fun GlowingBorderBox(
    glowColor: Color,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    glowRadius: Dp = 18.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.glowingBorder(glowColor, cornerRadius, glowRadius),
        content = content
    )
}
