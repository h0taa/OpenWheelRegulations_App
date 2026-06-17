package com.openwheel.regulations.data.model

/** Immutable snapshot of all user preferences, surfaced from DataStore. */
data class AppPreferences(
    val darkTheme: Boolean = true,
    val dynamicColor: Boolean = false,
    val showEraHighlights: Boolean = true,
    val showStaggerAnimation: Boolean = true
)
