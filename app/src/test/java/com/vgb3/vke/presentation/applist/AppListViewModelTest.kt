package com.vgb3.vke.presentation.applist

import com.vgb3.vke.domain.model.App
import com.vgb3.vke.domain.model.AppDetails
import com.vgb3.vke.domain.model.SimilarApp
import com.vgb3.vke.domain.repository.AppRepository
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AppListViewModelTest {

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadsCatalogAndExposesServerData() = runTest {
        val repository = FakeRepository { listOf(app) }
        val viewModel = AppListViewModel(repository)

        assertEquals(AppListUiState.Loading, viewModel.state.value)
        advanceUntilIdle()

        assertEquals(AppListUiState.Content(listOf(app)), viewModel.state.value)
        assertEquals(1, repository.calls)
    }

    @Test
    fun networkFailureCanBeRetriedSuccessfully() = runTest {
        val repository = FakeRepository { throw IOException("Offline") }
        val viewModel = AppListViewModel(repository)
        advanceUntilIdle()
        assertEquals(AppListUiState.Error, viewModel.state.value)

        repository.load = { listOf(app) }
        viewModel.loadApps()
        advanceUntilIdle()

        assertEquals(AppListUiState.Content(listOf(app)), viewModel.state.value)
        assertEquals(2, repository.calls)
    }

    @Test
    fun repeatedRetryDoesNotStartParallelRequests() = runTest {
        val repository = FakeRepository { awaitCancellation() }
        val viewModel = AppListViewModel(repository)
        advanceUntilIdle()

        viewModel.loadApps()
        viewModel.loadApps()
        advanceUntilIdle()

        assertEquals(1, repository.calls)
        assertEquals(AppListUiState.Loading, viewModel.state.value)
    }

    @Test
    fun cancellationIsNotShownAsNetworkFailure() = runTest {
        val viewModel = AppListViewModel(FakeRepository { throw CancellationException() })

        advanceUntilIdle()

        assertEquals(AppListUiState.Loading, viewModel.state.value)
    }

    @Test
    fun emptyCatalogIsNotAnError() = runTest {
        val viewModel = AppListViewModel(FakeRepository { emptyList() })

        advanceUntilIdle()

        assertEquals(AppListUiState.Content(emptyList()), viewModel.state.value)
    }

    @Test
    fun bothModesPublishRatingsAndKeepTheirTimingResults() = runTest {
        val vm = AppListViewModel(FakeRepository { listOf(app) })
        advanceUntilIdle()
        vm.loadRatingsSequentially()
        assertTrue((vm.state.value as AppListUiState.Content).ratingsLoading)
        advanceUntilIdle()
        val sequential = vm.state.value as AppListUiState.Content
        assertEquals(mapOf(app.id to 4.2f), sequential.ratings)
        assertNotNull(sequential.sequentialDurationMs)
        assertFalse(sequential.ratingsLoading)

        vm.loadRatingsInParallel()
        advanceUntilIdle()
        val parallel = vm.state.value as AppListUiState.Content
        assertEquals(mapOf(app.id to 4.5f), parallel.ratings)
        assertEquals(sequential.sequentialDurationMs, parallel.sequentialDurationMs)
        assertNotNull(parallel.parallelDurationMs)
    }

    @Test
    fun ratingsFailureKeepsCatalogVisibleAndCanBeRetried() = runTest {
        val repository = FakeRepository { listOf(app) }
        repository.sequential = { throw IOException("Unavailable") }
        val vm = AppListViewModel(repository)
        advanceUntilIdle()
        vm.loadRatingsSequentially()
        advanceUntilIdle()
        val failed = vm.state.value as AppListUiState.Content
        assertEquals(listOf(app), failed.apps)
        assertTrue(failed.ratingsError)
        assertFalse(failed.ratingsLoading)

        vm.loadRatingsInParallel()
        advanceUntilIdle()
        assertFalse((vm.state.value as AppListUiState.Content).ratingsError)
        assertEquals(mapOf(app.id to 4.5f), (vm.state.value as AppListUiState.Content).ratings)
    }

    @Test
    fun repeatedClicksCannotStartOverlappingRatingLoads() = runTest {
        val repository = FakeRepository { listOf(app) }
        repository.sequential = { awaitCancellation() }
        val vm = AppListViewModel(repository)
        advanceUntilIdle()
        vm.loadRatingsSequentially()
        vm.loadRatingsSequentially()
        vm.loadRatingsInParallel()
        runCurrent()
        assertEquals(1, repository.sequentialCalls)
        assertEquals(0, repository.parallelCalls)
        vm.loadApps()
        advanceUntilIdle()
        assertFalse((vm.state.value as AppListUiState.Content).ratingsLoading)
    }

    private class FakeRepository(var load: suspend () -> List<App>) : AppRepository {
        var calls = 0
        var sequentialCalls = 0
        var parallelCalls = 0
        var sequential: suspend (List<String>) -> Map<String, Float> = { ids -> ids.associateWith { 4.2f } }

        override suspend fun getApps(): List<App> {
            calls++
            return load()
        }

        override suspend fun getAppDetails(id: String): AppDetails = error("Not used")

        override suspend fun getSimilarApps(id: String): List<SimilarApp> = error("Not used")

        override suspend fun loadRatingsSequentially(ids: List<String>): Map<String, Float> {
            sequentialCalls++
            return sequential(ids)
        }

        override suspend fun loadRatingsInParallel(ids: List<String>): Map<String, Float> {
            parallelCalls++
            return ids.associateWith { 4.5f }
        }
    }

    private val app = App(
        id = "fa2e31b8-1234-4cf7-9914-108a170a1b01",
        title = "QuickNote",
        description = "Заметки",
        iconUrl = "https://example.com/icon.png",
    )
}
