package com.openwheel.regulations.data.repository

import android.content.Context
import app.cash.turbine.test
import com.openwheel.regulations.data.db.BookmarkDao
import com.openwheel.regulations.data.db.RegulationDao
import com.openwheel.regulations.sampleYear
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegulationRepositoryTest {

    private lateinit var regulationDao: RegulationDao
    private lateinit var bookmarkDao: BookmarkDao
    private lateinit var context: Context
    private lateinit var repository: RegulationRepository

    @Before
    fun setUp() {
        regulationDao = mockk(relaxed = true)
        bookmarkDao = mockk(relaxed = true)
        context = mockk(relaxed = true)
        repository = RegulationRepository(context, regulationDao, bookmarkDao)
    }

    @Test
    fun `getAllYears delegates to dao`() = runTest {
        val years = listOf(sampleYear(2022), sampleYear(2021))
        every { regulationDao.observeAll() } returns flowOf(years)

        repository.getAllYears().test {
            assertEquals(years, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `toggleBookmark adds when not bookmarked and returns true`() = runTest {
        coEvery { bookmarkDao.isBookmarked(2022) } returns false

        val result = repository.toggleBookmark(2022)

        assertTrue(result)
        coVerify(exactly = 1) { bookmarkDao.add(match { it.year == 2022 }) }
        coVerify(exactly = 0) { bookmarkDao.remove(any()) }
    }

    @Test
    fun `toggleBookmark removes when already bookmarked and returns false`() = runTest {
        coEvery { bookmarkDao.isBookmarked(2022) } returns true

        val result = repository.toggleBookmark(2022)

        assertFalse(result)
        coVerify(exactly = 1) { bookmarkDao.remove(2022) }
        coVerify(exactly = 0) { bookmarkDao.add(any()) }
    }

    @Test
    fun `deleteYear removes the row and its bookmark`() = runTest {
        val year = sampleYear(2009)

        repository.deleteYear(year)

        coVerify(exactly = 1) { regulationDao.delete(year) }
        coVerify(exactly = 1) { bookmarkDao.remove(2009) }
    }

    @Test
    fun `isBookmarked reflects the dao flow`() = runTest {
        every { bookmarkDao.observeIsBookmarked(2014) } returns flowOf(true)

        repository.isBookmarked(2014).test {
            assertTrue(awaitItem())
            awaitComplete()
        }
    }
}
