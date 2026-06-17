package com.openwheel.regulations.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openwheel.regulations.data.db.BookmarkDao
import com.openwheel.regulations.data.db.DatabaseSeeder
import com.openwheel.regulations.data.db.RegulationDao
import com.openwheel.regulations.data.db.RegulationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

/**
 * Provides the Room database and its DAOs.
 *
 * Seeding strategy: a [androidx.room.RoomDatabase.Callback] kicks off an idempotent
 * JSON seed on first creation, and the repository ALSO calls `ensureSeeded()` from the
 * Home screen so the very first frame is deterministic even if the async callback has
 * not completed yet. Both paths funnel through [DatabaseSeeder.seedIfEmpty], which is a
 * no-op once rows exist.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        // Provider<> breaks the cycle: the callback needs a DAO, but the DAO is obtained
        // from the very database we are building here.
        regulationDaoProvider: Provider<RegulationDao>
    ): RegulationDatabase {
        return Room.databaseBuilder(
            context,
            RegulationDatabase::class.java,
            RegulationDatabase.NAME
        )
            .addCallback(object : androidx.room.RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        DatabaseSeeder.seedIfEmpty(context, regulationDaoProvider.get())
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideRegulationDao(database: RegulationDatabase): RegulationDao =
        database.regulationDao()

    @Provides
    fun provideBookmarkDao(database: RegulationDatabase): BookmarkDao =
        database.bookmarkDao()
}
