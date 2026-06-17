package com.openwheel.regulations.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.openwheel.regulations.data.model.RegulationYear
import kotlinx.coroutines.flow.Flow

@Dao
interface RegulationDao {

    @Query("SELECT * FROM regulation_years ORDER BY year DESC")
    fun observeAll(): Flow<List<RegulationYear>>

    @Query("SELECT * FROM regulation_years WHERE year = :year LIMIT 1")
    fun observeYear(year: Int): Flow<RegulationYear?>

    @Query("SELECT * FROM regulation_years WHERE year = :year LIMIT 1")
    suspend fun getYear(year: Int): RegulationYear?

    @Query("SELECT COUNT(*) FROM regulation_years")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(years: List<RegulationYear>)

    /** Insert-or-replace a single row (used by the admin "Add" and "Save" flows). */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(year: RegulationYear)

    @Update
    suspend fun update(year: RegulationYear)

    @Delete
    suspend fun delete(year: RegulationYear)

    @Query("DELETE FROM regulation_years WHERE year = :year")
    suspend fun deleteByYear(year: Int)
}
