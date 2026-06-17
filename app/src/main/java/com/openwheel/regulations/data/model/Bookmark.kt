package com.openwheel.regulations.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/** A bookmarked season, keyed by year. */
@Entity(tableName = "bookmarks")
data class Bookmark(
    @PrimaryKey val year: Int,
    val addedAt: Long = System.currentTimeMillis()
)
