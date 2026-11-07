package com.vgb3.vke.presentation.details

import com.vgb3.vke.domain.model.App
import com.vgb3.vke.domain.model.AppDetails
import com.vgb3.vke.domain.model.SimilarApp
import com.vgb3.vke.domain.repository.AppRepository
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadsDetailsForSelectedUuid() = runTest {
        val repository = FakeRepository { details }
        val viewModel = DetailsViewModel(details.id, repository)

        assertEquals(DetailsUiState.Loading, viewModel.state.value)
        advanceUntilIdle()

        assertEquals(listOf(details.id), repository.requestedIds)
        assertEquals(DetailsUiState.Content(details, emptyList()), viewModel.state.value)
    }

    @Test
    fun failedDetailsCanBeRetried() = runTest {
        val repository = FakeRepository { throw IOException("Offline") }
        val viewModel = DetailsViewModel(details.id, repository)
        advanceUntilIdle()
        assertEquals(DetailsUiState.Error, viewModel.state.value)

        repository.loadDetails = { details }
        viewModel.loadDetails()
        advanceUntilIdle()

        assertEquals(listOf(details.id, details.id), repository.requestedIds)
        assertEquals(DetailsUiState.Content(details, emptyList()), viewModel.state.value)
    }

    @Test
    fun failedOtherAppsDoNotHideLoadedDetails() = runTest {
        val repository = FakeRepository { details }
        repository.loadSimilar = { throw IOException("Catalog unavailable") }
        val viewModel = DetailsViewModel(details.id, repository)

        advanceUntilIdle()

        assertEquals(DetailsUiState.Content(details, emptyList()), viewModel.state.value)
    }

    @Test
    fun detailsAppearBeforeOtherAppsFinishLoading() = runTest {
        val otherApps = CompletableDeferred<List<SimilarApp>>()
        val repository = FakeRepository { details }
        repository.loadSimilar = { otherApps.await() }
        val viewModel = DetailsViewModel(details.id, repository)
        runCurrent()

        assertEquals(DetailsUiState.Content(details, emptyList()), viewModel.state.value)

        val similarApp = SimilarApp("another-id", "Calmalist", "https://example.com/icon.png")
        otherApps.complete(listOf(similarApp))
        advanceUntilIdle()

        assertEquals(DetailsUiState.Content(details, listOf(similarApp)), viewModel.state.value)
    }

    @Test
    fun repeatedRetryDoesNotStartParallelDetailsRequests() = runTest {
        val pendingDetails = CompletableDeferred<AppDetails>()
        val repository = FakeRepository { pendingDetails.await() }
        val viewModel = DetailsViewModel(details.id, repository)
        runCurrent()

        viewModel.loadDetails()
        viewModel.loadDetails()
        runCurrent()

        assertEquals(listOf(details.id), repository.requestedIds)
        pendingDetails.complete(details)
        advanceUntilIdle()
    }

    @Test
    fun detailsCancellationIsNotDisplayedAsError() = runTest {
        val viewModel = DetailsViewModel(details.id, FakeRepository { throw CancellationException() })

        advanceUntilIdle()

        assertEquals(DetailsUiState.Loading, viewModel.state.value)
    }

    @Test
    fun otherAppsCancellationKeepsVisibleDetails() = runTest {
        val repository = FakeRepository { details }
        repository.loadSimilar = { throw CancellationException() }
        val viewModel = DetailsViewModel(details.id, repository)

        advanceUntilIdle()

        assertEquals(DetailsUiState.Content(details, emptyList()), viewModel.state.value)
    }

    private class FakeRepository(var loadDetails: suspend () -> AppDetails) : AppRepository {
        val requestedIds = mutableListOf<String>()
        var loadSimilar: suspend () -> List<SimilarApp> = { emptyList() }

        override suspend fun getApps(): List<App> = error("Not used")

        override suspend fun getAppDetails(id: String): AppDetails {
            requestedIds += id
            return loadDetails()
        }

        override suspend fun getSimilarApps(id: String): List<SimilarApp> = loadSimilar()
    }

    private val details = AppDetails(
        id = "fa2e31b8-1234-4cf7-9914-108a170a1b01",
        name = "QuickNote",
        developer = "QuickSoft Inc.",
        category = "Производительность",
        age = 3,
        appSize = 8.2,
        screenshotItems = listOf("https://example.com/screenshot.png"),
        description = "Полное описание приложения",
        iconApp = "https://example.com/icon.png",
    )
}
