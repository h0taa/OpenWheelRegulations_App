package com.openwheel.regulations.data.repository

import android.content.Context
import com.openwheel.regulations.data.db.BookmarkDao
import com.openwheel.regulations.data.db.DatabaseSeeder
import com.openwheel.regulations.data.db.RegulationDao
import com.openwheel.regulations.data.model.Bookmark
import com.openwheel.regulations.data.model.RegulationYear
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/** Single source of truth for regulation data and bookmarks. */
@Singleton
class RegulationRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val regulationDao: RegulationDao,
    private val bookmarkDao: BookmarkDao
) {

    fun getAllYears(): Flow<List<RegulationYear>> = regulationDao.observeAll()

    fun getYear(year: Int): Flow<RegulationYear?> = regulationDao.observeYear(year)

    suspend fun getYearOnce(year: Int): RegulationYear? = regulationDao.getYear(year)

    fun getBookmarkedYears(): Flow<List<Int>> = bookmarkDao.observeBookmarkedYears()

    fun getBookmarks(): Flow<List<Bookmark>> = bookmarkDao.observeAll()

    fun isBookmarked(year: Int): Flow<Boolean> = bookmarkDao.observeIsBookmarked(year)

    /** Guarantees the database has been populated. Safe to call repeatedly. */
    suspend fun ensureSeeded() = DatabaseSeeder.seedIfEmpty(context, regulationDao)

    /** Toggles the bookmark for [year] and returns the new state (true = now bookmarked). */
    suspend fun toggleBookmark(year: Int): Boolean {
        return if (bookmarkDao.isBookmarked(year)) {
            bookmarkDao.remove(year)
            false
        } else {
            bookmarkDao.add(Bookmark(year = year))
            true
        }
    }

    // ---- Admin CRUD ----

    suspend fun upsertYear(year: RegulationYear) = regulationDao.upsert(year)

    suspend fun updateYear(year: RegulationYear) = regulationDao.update(year)

    suspend fun deleteYear(year: RegulationYear) {
        regulationDao.delete(year)
        bookmarkDao.remove(year.year)
    }
}
