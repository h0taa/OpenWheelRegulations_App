package com.openwheel.regulations.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.openwheel.regulations.data.model.Bookmark
import com.openwheel.regulations.data.model.RegulationYear

@Database(
    entities = [RegulationYear::class, Bookmark::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RegulationDatabase : RoomDatabase() {
    abstract fun regulationDao(): RegulationDao
    abstract fun bookmarkDao(): BookmarkDao

    companion object {
        const val NAME = "regulations.db"
    }
}
