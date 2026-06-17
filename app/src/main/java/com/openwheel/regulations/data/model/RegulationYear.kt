package com.openwheel.regulations.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * A single season's regulation summary.
 *
 * This class is BOTH a Room [Entity] and a kotlinx [Serializable] DTO so that the
 * exact same shape can be parsed from `assets/regulations_data.json` and stored in
 * the database. List fields are persisted as JSON strings via the registered
 * TypeConverters (see [com.openwheel.regulations.data.db.Converters]).
 */
@Serializable
@Entity(tableName = "regulation_years")
data class RegulationYear(
    @PrimaryKey val year: Int,
    val eraName: String,
    val headline: String,
    val keyChanges: List<String> = emptyList(),
    val carDimensions: String = "",
    val powerUnit: String = "",
    val aerodynamicRules: String = "",
    val tyreRegulations: String = "",
    val notableImpact: String = "",
    val funFacts: List<String> = emptyList(),
    val isMajorChange: Boolean = false,
    val visualComponents: List<VisualChange> = emptyList()
)

/** Wrapper matching the top level of `regulations_data.json`. */
@Serializable
data class RegulationDataFile(
    val schemaVersion: Int = 1,
    val years: List<RegulationYear> = emptyList()
)
