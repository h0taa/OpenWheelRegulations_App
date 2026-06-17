package com.openwheel.regulations.util

/** App-wide constants. Regulation *data* lives in the database; these are UI/behaviour knobs. */
object Constants {

    /** Years that receive the distinctive "era change" treatment on the Home screen. */
    val MAJOR_CHANGE_YEARS: Set<Int> = setOf(
        2026, 2022, 2017, 2014, 2009, 2005, 2003, 2001, 2000
    )

    val FIRST_YEAR = 2000
    val LATEST_YEAR = 2026

    // ---- Animation timing ----
    /** Per-item delay for the Home list stagger entrance. */
    const val STAGGER_ITEM_DELAY_MS = 60L

    /** Total splash duration target (ms) across all six animation steps. */
    const val SPLASH_TOTAL_MS = 3_000L

    // ---- Misc ----
    const val APP_VERSION_NAME = "1.0.0"
    const val APP_VERSION_CODE = 1
}
