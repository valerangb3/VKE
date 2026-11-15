package com.vgb3.vke.presentation.details

import androidx.lifecycle.ViewModelStore
import com.vgb3.vke.domain.model.App
import com.vgb3.vke.domain.model.AppDetails
import com.vgb3.vke.domain.model.SimilarApp
import com.vgb3.vke.domain.repository.AppRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
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
class InstallViewModelTest {
    @Before
    fun setUp() = Dispatchers.setMain(StandardTestDispatcher())

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun installationProgressCompletesAndInstalledAppCannotStartAgain() = runTest {
        val vm = DetailsViewModel(details.id, repository())
        advanceUntilIdle()

        vm.onInstallClick()
        assertEquals(InstallState.Installing(0), vm.content().installState)
        runCurrent()
        advanceTimeBy(150)
        runCurrent()
        assertEquals(InstallState.Installing(5), vm.content().installState)

        advanceUntilIdle()
        assertEquals(InstallState.Installed, vm.content().installState)
        vm.onInstallClick()
        advanceUntilIdle()
        assertEquals(InstallState.Installed, vm.content().installState)
    }

    @Test
    fun cancellationResetsProgressAndCancelledJobCannotFinishInstallation() = runTest {
        val vm = DetailsViewModel(details.id, repository())
        advanceUntilIdle()
        vm.onInstallClick()
        runCurrent()
        advanceTimeBy(750)
        runCurrent()
        assertEquals(InstallState.Installing(25), vm.content().installState)

        vm.onInstallClick()
        assertEquals(InstallState.NotInstalled, vm.content().installState)
        advanceUntilIdle()
        assertEquals(InstallState.NotInstalled, vm.content().installState)

        vm.onInstallClick()
        assertEquals(InstallState.Installing(0), vm.content().installState)
        advanceUntilIdle()
        assertEquals(InstallState.Installed, vm.content().installState)
    }

    @Test
    fun rapidStartCancelStartDoesNotLeaveAnOldInstallationRunning() = runTest {
        val vm = DetailsViewModel(details.id, repository())
        advanceUntilIdle()
        vm.onInstallClick()
        vm.onInstallClick()
        assertEquals(InstallState.NotInstalled, vm.content().installState)
        vm.onInstallClick()
        runCurrent()
        advanceTimeBy(150)
        runCurrent()
        assertEquals(InstallState.Installing(5), vm.content().installState)
        vm.onInstallClick()
        advanceUntilIdle()
        assertEquals(InstallState.NotInstalled, vm.content().installState)
    }

    @Test
    fun similarAppsCompletionPreservesInstallationProgress() = runTest {
        val pending = CompletableDeferred<List<SimilarApp>>()
        val vm = DetailsViewModel(details.id, repository { pending.await() })
        runCurrent()
        vm.onInstallClick()
        runCurrent()
        advanceTimeBy(300)
        runCurrent()

        pending.complete(listOf(SimilarApp("other", "Other", "")))
        runCurrent()
        assertEquals(InstallState.Installing(10), vm.content().installState)
        assertEquals("other", vm.content().similarApps.single().id)
        vm.onInstallClick()
        advanceUntilIdle()
    }

    @Test
    fun clearingViewModelCancelsInstallationThroughItsScope() = runTest {
        val vm = DetailsViewModel(details.id, repository())
        val store = ViewModelStore().apply { put("details", vm) }
        advanceUntilIdle()
        vm.onInstallClick()
        runCurrent()
        advanceTimeBy(150)
        runCurrent()
        val beforeClear = vm.state.value

        store.clear()
        advanceUntilIdle()
        assertEquals(beforeClear, vm.state.value)
    }

    @Test
    fun clickBeforeContentLoadsDoesNotStartInstallation() = runTest {
        val vm = DetailsViewModel(details.id, repository())
        vm.onInstallClick()
        advanceUntilIdle()
        assertEquals(InstallState.NotInstalled, vm.content().installState)
    }

    private fun DetailsViewModel.content() = state.value as DetailsUiState.Content

    private fun repository(similar: suspend () -> List<SimilarApp> = { emptyList() }) = object : AppRepository {
        override suspend fun getApps(): List<App> = error("Not used")
        override suspend fun getAppDetails(id: String): AppDetails = details
        override suspend fun getSimilarApps(id: String): List<SimilarApp> = similar()
        override suspend fun loadRatingsSequentially(ids: List<String>): Map<String, Float> = error("Not used")
        override suspend fun loadRatingsInParallel(ids: List<String>): Map<String, Float> = error("Not used")
    }

    private val details = AppDetails(
        id = "quick-note", name = "QuickNote", developer = "QuickSoft", category = "Заметки",
        age = 3, appSize = 8.2, screenshotItems = emptyList(), description = "Заметки", iconApp = "",
    )
}
