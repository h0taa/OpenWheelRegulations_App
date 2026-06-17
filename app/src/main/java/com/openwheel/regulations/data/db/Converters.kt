package com.openwheel.regulations.data.db

import androidx.room.TypeConverter
import com.openwheel.regulations.data.model.VisualChange
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Room TypeConverters that persist the list fields of
 * [com.openwheel.regulations.data.model.RegulationYear] as JSON strings.
 */
class Converters {

    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromStringList(value: List<String>): String = json.encodeToString(value)

    @TypeConverter
    fun toStringList(value: String): List<String> =
        if (value.isBlank()) emptyList() else json.decodeFromString(value)

    @TypeConverter
    fun fromVisualChangeList(value: List<VisualChange>): String = json.encodeToString(value)

    @TypeConverter
    fun toVisualChangeList(value: String): List<VisualChange> =
        if (value.isBlank()) emptyList() else json.decodeFromString(value)
}
