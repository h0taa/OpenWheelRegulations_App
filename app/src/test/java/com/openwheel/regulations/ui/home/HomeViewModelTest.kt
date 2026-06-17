package com.openwheel.regulations.ui.home

import app.cash.turbine.test
import com.openwheel.regulations.MainDispatcherRule
import com.openwheel.regulations.data.model.AppPreferences
import com.openwheel.regulations.data.repository.PreferencesRepository
import com.openwheel.regulations.data.repository.RegulationRepository
import com.openwheel.regulations.sampleYear
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: RegulationRepository
    private lateinit var preferencesRepository: PreferencesRepository

    private val years = listOf(
        sampleYear(2022, eraName = "Ground Effect", headline = "Cars redesigned"),
        sampleYear(2014, eraName = "V6 Turbo Hybrid", headline = "New power units"),
        sampleYear(2009, eraName = "Aero Overhaul", headline = "KERS introduced")
    )

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        preferencesRepository = mockk(relaxed = true)

        every { repository.getAllYears() } returns flowOf(years)
        every { repository.getBookmarkedYears() } returns flowOf(emptyList())
        every { preferencesRepository.preferences } returns flowOf(AppPreferences())
        coEvery { repository.ensureSeeded() } just Runs
    }

    private fun viewModel() = HomeViewModel(repository, preferencesRepository)

    @Test
    fun `loads all years with no search query`() = runTest {
        viewModel().uiState.test {
            var state = awaitItem()
            while (state.isLoading) state = awaitItem()

            assertEquals(3, state.visibleYears.size)
            assertEquals(3, state.allYears.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search query filters visible years`() = runTest {
        val vm = viewModel()
        vm.uiState.test {
            var state = awaitItem()
            while (state.isLoading) state = awaitItem()

            vm.onSearchQueryChange("hybrid")

            var filtered = awaitItem()
            while (filtered.searchQuery != "hybrid") filtered = awaitItem()

            assertEquals(1, filtered.visibleYears.size)
            assertEquals(2014, filtered.visibleYears.first().year)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search matches on year number`() = runTest {
        val vm = viewModel()
        vm.uiState.test {
            var state = awaitItem()
            while (state.isLoading) state = awaitItem()

            vm.onSearchQueryChange("2009")
            var filtered = awaitItem()
            while (filtered.searchQuery != "2009") filtered = awaitItem()

            assertTrue(filtered.visibleYears.all { it.year == 2009 })
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleBookmark calls repository`() = runTest {
        coEvery { repository.toggleBookmark(2022) } returns true
        val vm = viewModel()

        vm.toggleBookmark(2022)

        coVerify(exactly = 1) { repository.toggleBookmark(2022) }
    }
}
