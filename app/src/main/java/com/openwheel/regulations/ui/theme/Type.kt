package com.openwheel.regulations.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * ===========================================================================
 *  FONTS — read me
 * ===========================================================================
 * The spec calls for two embedded local fonts:
 *   • Orbitron  -> headers and big year numbers
 *   • Inter     -> body text
 *
 * Binary .ttf files cannot be generated as text, so this file currently maps
 * both roles onto the system font ([FontFamily.Default] / a monospace-ish
 * fallback for the display role) so the project compiles and runs immediately.
 *
 * TO ENABLE THE REAL FONTS (≈2 minutes — see DOWNLOAD_FONTS.md):
 *   1. Run the helper script, or download the OFL .ttf files from Google Fonts:
 *        res/font/orbitron_bold.ttf, res/font/orbitron_semibold.ttf
 *        res/font/inter_regular.ttf, res/font/inter_medium.ttf,
 *        res/font/inter_semibold.ttf
 *   2. Uncomment the FontFamily definitions below and replace the two
 *      `= FontFamily.Default` assignments with `Orbitron` / `Inter`.
 * ---------------------------------------------------------------------------
 */

/*  // <-- STEP 2: delete this line and the closing one to enable.
import androidx.compose.ui.text.font.Font
import com.openwheel.regulations.R

val Orbitron = FontFamily(
    Font(R.font.orbitron_semibold, FontWeight.SemiBold),
    Font(R.font.orbitron_bold, FontWeight.Bold)
)

val Inter = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold)
)
*/

/** Header / display family (Orbitron once enabled). */
val DisplayFontFamily: FontFamily = FontFamily.Default

/** Body family (Inter once enabled). */
val BodyFontFamily: FontFamily = FontFamily.Default

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 72.sp,
        lineHeight = 76.sp,
        letterSpacing = 1.sp
    ),
    displayMedium = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.5.sp
    ),
    displaySmall = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 34.sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp,
        lineHeight = 32.sp
    ),
    titleLarge = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 1.sp
    ),
    titleMedium = TextStyle(
        fontFamily = BodyFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = BodyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = BodyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontFamily = BodyFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.8.sp
    ),
    labelMedium = TextStyle(
        fontFamily = BodyFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp
    )
)
