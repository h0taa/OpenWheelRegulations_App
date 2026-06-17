package com.openwheel.regulations.util

import androidx.compose.ui.graphics.Color

/** Returns the era label for a season, used as the card subtitle. */
fun eraSubtitleFor(year: Int): String = when (year) {
    in 2026..2026 -> "New Hybrid & Active-Aero Era"
    in 2022..2025 -> "Ground-Effect Era"
    in 2017..2021 -> "Wide-Car Hybrid Era"
    in 2014..2016 -> "V6 Turbo-Hybrid Era"
    in 2009..2013 -> "Aero-Overhaul & KERS Era"
    in 2006..2008 -> "V8 Era"
    in 2005..2005 -> "Endurance-Tyre Era"
    in 2000..2004 -> "V10 Era"
    else -> "Formula 1"
}

/** Linearly interpolates between two colors; [fraction] is clamped to 0f..1f. */
fun lerpColor(start: Color, stop: Color, fraction: Float): Color {
    val f = fraction.coerceIn(0f, 1f)
    return Color(
        red = start.red + (stop.red - start.red) * f,
        green = start.green + (stop.green - start.green) * f,
        blue = start.blue + (stop.blue - start.blue) * f,
        alpha = start.alpha + (stop.alpha - start.alpha) * f
    )
}
