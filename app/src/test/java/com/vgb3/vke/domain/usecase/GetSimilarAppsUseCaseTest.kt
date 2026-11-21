package com.vgb3.vke.domain.usecase

import com.vgb3.vke.domain.model.SimilarApp
import com.vgb3.vke.testutil.FakeAppRepository
import com.vgb3.vke.testutil.assertSuspendThrows
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class GetSimilarAppsUseCaseTest {
    private val repository = FakeAppRepository()
    private val useCase = GetSimilarAppsUseCase(repository)

    @Test
    fun returnsSimilarAppsInRepositoryOrderAndForwardsSelectedId() = runTest {
        val apps = listOf(SimilarApp("second", "Second", "second.png"), SimilarApp("first", "First", "first.png"))
        repository.similar = { apps }

        assertEquals(apps, useCase("selected"))
        assertEquals(listOf("selected"), repository.similarIds)
    }

    @Test
    fun acceptsNoSimilarApps() = runTest {
        repository.similar = { emptyList() }

        assertEquals(emptyList<SimilarApp>(), useCase("selected"))
        assertEquals(listOf("selected"), repository.similarIds)
    }

    @Test
    fun eachInvocationUsesItsOwnIdAndFreshResult() = runTest {
        repository.similar = { id -> listOf(SimilarApp("similar-to-$id", "Other", "")) }

        assertEquals(listOf(SimilarApp("similar-to-first", "Other", "")), useCase("first"))
        assertEquals(listOf(SimilarApp("similar-to-second", "Other", "")), useCase("second"))
        assertEquals(listOf("first", "second"), repository.similarIds)
    }

    @Test
    fun propagatesRepositoryFailureWithoutRetryOrFallback() = runTest {
        val failure = IOException("Catalog unavailable")
        repository.similar = { throw failure }

        assertSame(failure, assertSuspendThrows<IOException> { useCase("selected") })
        assertEquals(listOf("selected"), repository.similarIds)
    }

    @Test
    fun propagatesCancellation() = runTest {
        val cancellation = CancellationException("Cancelled")
        repository.similar = { throw cancellation }

        assertSame(cancellation, assertSuspendThrows<CancellationException> { useCase("selected") })
    }
}
