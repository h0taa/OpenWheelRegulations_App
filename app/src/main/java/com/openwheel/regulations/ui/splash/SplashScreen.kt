package com.openwheel.regulations.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.openwheel.regulations.ui.components.OwrLogo
import com.openwheel.regulations.ui.theme.AccentRed
import com.openwheel.regulations.ui.theme.DarkBg
import com.openwheel.regulations.ui.theme.OffWhite
import kotlinx.coroutines.delay

/**
 * Launch animation:
 *  1. Background fades in.
 *  2. The OWR logo (car) scales in with a spring overshoot.
 *  3. The app name fades in and slides up.
 *  4. A red "loading" line draws left -> right beneath the name.
 *  5. Brief hold, then everything fades out and [onFinished] navigates Home.
 *
 * Layout: car, then the title, then the loading line — with equal gaps above and below
 * the title so it sits centred between the car and the line.
 */
@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val density = LocalDensity.current

    val bgAlpha = remember { Animatable(0f) }
    val logoScale = remember { Animatable(0f) }
    val nameAlpha = remember { Animatable(0f) }
    val nameOffsetDp = remember { Animatable(24f) } // slides up to 0
    val lineProgress = remember { Animatable(0f) }
    val rootAlpha = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        bgAlpha.animateTo(1f, tween(400))

        logoScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )

        nameAlpha.animateTo(1f, tween(350))
        nameOffsetDp.animateTo(0f, tween(350))

        // Loading line draws across, beneath the title.
        lineProgress.animateTo(1f, tween(450, easing = LinearEasing))

        delay(650)

        rootAlpha.animateTo(0f, tween(400))
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .alpha(rootAlpha.value)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .alpha(bgAlpha.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(1f))

            // Car
            OwrLogo(
                modifier = Modifier
                    .size(132.dp)
                    .scale(logoScale.value)
            )

            // Equal gap above the title…
            Spacer(Modifier.weight(1f))

            // Title (centred between the car and the loading line)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .alpha(nameAlpha.value)
                    .offset(y = nameOffsetDp.value.dp)
            ) {
                Text(
                    text = "OPEN WHEEL",
                    style = MaterialTheme.typography.titleLarge,
                    color = OffWhite
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "REGULATIONS",
                    style = MaterialTheme.typography.titleMedium,
                    color = AccentRed
                )
            }

            // …equal gap below the title, then the loading line.
            Spacer(Modifier.weight(1f))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
            ) {
                val totalWidth = size.width * 0.6f
                val startX = (size.width - totalWidth) / 2f
                val endX = startX + totalWidth * lineProgress.value
                val y = size.height / 2f
                drawLine(
                    color = AccentRed,
                    start = Offset(startX, y),
                    end = Offset(endX, y),
                    strokeWidth = with(density) { 3.dp.toPx() },
                    cap = StrokeCap.Round
                )
            }

            Spacer(Modifier.weight(1.25f))
        }
    }
}
