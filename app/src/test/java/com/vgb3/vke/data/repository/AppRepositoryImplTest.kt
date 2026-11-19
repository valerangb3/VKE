package com.vgb3.vke.data.repository

import com.vgb3.vke.domain.model.SimilarApp
import com.vgb3.vke.testutil.FakeCatalogApi
import com.vgb3.vke.testutil.TestData.app
import com.vgb3.vke.testutil.TestData.appDto
import com.vgb3.vke.testutil.TestData.details
import com.vgb3.vke.testutil.TestData.detailsDto
import com.vgb3.vke.testutil.assertSuspendThrows
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class AppRepositoryImplTest {
    private val api = FakeCatalogApi()
    private val repository = AppRepositoryImpl(api)

    @Test
    fun getAppsMapsEveryItemAndPreservesServerOrder() = runTest {
        api.apps = { listOf(appDto.copy(id = "second"), appDto) }

        assertEquals(listOf(app.copy(id = "second"), app), repository.getApps())
        assertEquals(1, api.appsCalls)
    }

    @Test
    fun getAppsAcceptsEmptyCatalog() = runTest {
        api.apps = { emptyList() }

        assertEquals(emptyList<Any>(), repository.getApps())
        assertEquals(1, api.appsCalls)
    }

    @Test
    fun getAppsReadsFreshDataOnEveryCall() = runTest {
        api.apps = { listOf(appDto) }
        assertEquals(listOf(app), repository.getApps())
        api.apps = { listOf(appDto.copy(name = "Updated")) }

        assertEquals(listOf(app.copy(title = "Updated")), repository.getApps())
        assertEquals(2, api.appsCalls)
    }

    @Test
    fun getAppsPropagatesApiFailure() = runTest {
        val failure = IOException("Offline")
        api.apps = { throw failure }

        assertSame(failure, assertSuspendThrows<IOException> { repository.getApps() })
        assertEquals(1, api.appsCalls)
    }

    @Test
    fun getAppsPropagatesCancellation() = runTest {
        val cancellation = CancellationException("Cancelled")
        api.apps = { throw cancellation }

        assertSame(cancellation, assertSuspendThrows<CancellationException> { repository.getApps() })
    }

    @Test
    fun getDetailsUsesCatalogRouteWithSelectedIdAndMapsAllFields() = runTest {
        api.details = { detailsDto }

        assertEquals(details, repository.getAppDetails(details.id))
        assertEquals(listOf(details.id), api.detailsIds)
        assertEquals(0, api.appsCalls)
    }

    @Test
    fun getDetailsNormalizesMissingOptionalFields() = runTest {
        api.details = { detailsDto.copy(iconUrl = null, screenshotUrlList = null, description = null) }

        assertEquals(
            details.copy(iconApp = "", screenshotItems = emptyList(), description = ""),
            repository.getAppDetails(details.id),
        )
    }

    @Test
    fun getDetailsPropagatesApiFailure() = runTest {
        val failure = IOException("Details unavailable")
        api.details = { throw failure }

        assertSame(failure, assertSuspendThrows<IOException> { repository.getAppDetails(details.id) })
        assertEquals(listOf(details.id), api.detailsIds)
    }

    @Test
    fun getDetailsPropagatesCancellation() = runTest {
        val cancellation = CancellationException("Cancelled")
        api.details = { throw cancellation }

        assertSame(cancellation, assertSuspendThrows<CancellationException> {
            repository.getAppDetails(details.id)
        })
    }

    @Test
    fun similarAppsExcludeSelectedIdAndPreserveRemainingOrderAndFields() = runTest {
        api.apps = { listOf(appDto.copy(id = "first"), appDto, appDto.copy(id = "last", iconUrl = null)) }

        assertEquals(
            listOf(SimilarApp("first", app.title, app.iconUrl), SimilarApp("last", app.title, "")),
            repository.getSimilarApps(app.id),
        )
        assertEquals(1, api.appsCalls)
    }

    @Test
    fun similarAppsAreEmptyWhenCatalogOnlyContainsSelectedApp() = runTest {
        api.apps = { listOf(appDto) }

        assertEquals(emptyList<SimilarApp>(), repository.getSimilarApps(app.id))
    }

    @Test
    fun similarAppsAreEmptyForEmptyCatalog() = runTest {
        api.apps = { emptyList() }

        assertEquals(emptyList<SimilarApp>(), repository.getSimilarApps(app.id))
    }

    @Test
    fun similarAppsReturnWholeCatalogWhenSelectedIdIsAbsent() = runTest {
        api.apps = { listOf(appDto) }

        assertEquals(listOf(SimilarApp(app.id, app.title, app.iconUrl)), repository.getSimilarApps("missing"))
    }

    @Test
    fun similarAppsPropagateApiFailure() = runTest {
        val failure = IOException("Catalog unavailable")
        api.apps = { throw failure }

        assertSame(failure, assertSuspendThrows<IOException> { repository.getSimilarApps(app.id) })
    }

    @Test
    fun similarAppsPropagateCancellation() = runTest {
        val cancellation = CancellationException("Cancelled")
        api.apps = { throw cancellation }

        assertSame(cancellation, assertSuspendThrows<CancellationException> {
            repository.getSimilarApps(app.id)
        })
    }
}
