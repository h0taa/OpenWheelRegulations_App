package com.openwheel.regulations.data.db

import android.content.Context
import com.openwheel.regulations.data.model.RegulationDataFile
import kotlinx.serialization.json.Json

/**
 * Seeds the database from the bundled JSON asset.
 *
 * NOTE: The original spec suggested `Room.databaseBuilder().createFromAsset(...)`,
 * but that API expects a *pre-built SQLite .db* file, not JSON. Parsing the JSON on
 * first launch (below) is the idiomatic, build-safe way to pre-populate Room from a
 * human-readable data file, and it lets the admin panel edit the data afterwards.
 */
object DatabaseSeeder {

    const val ASSET_FILE_NAME = "regulations_data.json"

    private val json = Json { ignoreUnknownKeys = true }

    /** Reads and parses the asset file. */
    fun loadFromAssets(context: Context): RegulationDataFile {
        val text = context.assets.open(ASSET_FILE_NAME)
            .bufferedReader()
            .use { it.readText() }
        return json.decodeFromString(text)
    }

    /** Inserts the seed data if (and only if) the table is currently empty. Idempotent. */
    suspend fun seedIfEmpty(context: Context, dao: RegulationDao) {
        if (dao.count() > 0) return
        val data = loadFromAssets(context)
        dao.insertAll(data.years)
    }
}
