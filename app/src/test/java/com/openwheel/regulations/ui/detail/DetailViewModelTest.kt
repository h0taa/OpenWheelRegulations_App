package com.openwheel.regulations.ui.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.openwheel.regulations.MainDispatcherRule
import com.openwheel.regulations.data.repository.RegulationRepository
import com.openwheel.regulations.sampleYear
import com.openwheel.regulations.ui.navigation.NavRoutes
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: RegulationRepository = mockk(relaxed = true)

    private fun viewModel(year: Int): DetailViewModel {
        val handle = SavedStateHandle(mapOf(NavRoutes.Detail.ARG_YEAR to year))
        return DetailViewModel(repository, handle)
    }

    @Test
    fun `loads the year referenced by the nav argument`() = runTest {
        every { repository.getYear(2022) } returns flowOf(sampleYear(2022))
        every { repository.isBookmarked(2022) } returns flowOf(false)

        viewModel(2022).uiState.test {
            var state = awaitItem()
            while (state.isLoading) state = awaitItem()

            assertNotNull(state.year)
            assertEquals(2022, state.year?.year)
            assertFalse(state.isBookmarked)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `reflects bookmarked state`() = runTest {
        every { repository.getYear(2014) } returns flowOf(sampleYear(2014))
        every { repository.isBookmarked(2014) } returns flowOf(true)

        viewModel(2014).uiState.test {
            var state = awaitItem()
            while (state.isLoading || !state.isBookmarked) state = awaitItem()

            assertEquals(true, state.isBookmarked)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleBookmark delegates to repository with the bound year`() = runTest {
        every { repository.getYear(2009) } returns flowOf(sampleYear(2009))
        every { repository.isBookmarked(2009) } returns flowOf(false)
        coEvery { repository.toggleBookmark(2009) } returns true

        val vm = viewModel(2009)
        vm.toggleBookmark()

        coVerify(exactly = 1) { repository.toggleBookmark(2009) }
    }
}
