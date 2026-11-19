package com.vgb3.vke.domain.usecase

import com.vgb3.vke.testutil.FakeAppRepository
import com.vgb3.vke.testutil.TestData.app
import com.vgb3.vke.testutil.assertSuspendThrows
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class GetAppsUseCaseTest {
    private val repository = FakeAppRepository()
    private val useCase = GetAppsUseCase(repository)

    @Test
    fun returnsCatalogInRepositoryOrderWithOneRequest() = runTest {
        val apps = listOf(app.copy(id = "second"), app)
        repository.apps = { apps }

        assertEquals(apps, useCase())
        assertEquals(1, repository.appsCalls)
    }

    @Test
    fun acceptsEmptyCatalog() = runTest {
        repository.apps = { emptyList() }

        assertEquals(emptyList<Any>(), useCase())
        assertEquals(1, repository.appsCalls)
    }

    @Test
    fun repeatedInvocationLoadsFreshCatalog() = runTest {
        repository.apps = { listOf(app) }
        assertEquals(listOf(app), useCase())
        repository.apps = { emptyList() }

        assertEquals(emptyList<Any>(), useCase())
        assertEquals(2, repository.appsCalls)
    }

    @Test
    fun propagatesRepositoryFailureWithoutRetryOrFallback() = runTest {
        val failure = IOException("Offline")
        repository.apps = { throw failure }

        assertSame(failure, assertSuspendThrows<IOException> { useCase() })
        assertEquals(1, repository.appsCalls)
    }

    @Test
    fun propagatesCancellation() = runTest {
        val cancellation = CancellationException("Cancelled")
        repository.apps = { throw cancellation }

        assertSame(cancellation, assertSuspendThrows<CancellationException> { useCase() })
    }
}
