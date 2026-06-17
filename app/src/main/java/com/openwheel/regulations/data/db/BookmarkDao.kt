package com.openwheel.regulations.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openwheel.regulations.data.model.Bookmark
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM bookmarks ORDER BY addedAt DESC")
    fun observeAll(): Flow<List<Bookmark>>

    @Query("SELECT year FROM bookmarks")
    fun observeBookmarkedYears(): Flow<List<Int>>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE year = :year)")
    fun observeIsBookmarked(year: Int): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE year = :year)")
    suspend fun isBookmarked(year: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(bookmark: Bookmark)

    @Query("DELETE FROM bookmarks WHERE year = :year")
    suspend fun remove(year: Int)
}
