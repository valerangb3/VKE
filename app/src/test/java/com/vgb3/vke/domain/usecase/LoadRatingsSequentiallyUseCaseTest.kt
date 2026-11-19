package com.vgb3.vke.domain.usecase

import com.vgb3.vke.testutil.FakeAppRepository
import com.vgb3.vke.testutil.assertSuspendThrows
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class LoadRatingsSequentiallyUseCaseTest {
    private val repository = FakeAppRepository()
    private val useCase = LoadRatingsSequentiallyUseCase(repository)

    @Test
    fun usesSequentialMethodAndPreservesIdOrderAndDuplicates() = runTest {
        val ids = listOf("second", "first", "second")
        val ratings = mapOf("first" to 3.5f, "second" to 4.7f)
        repository.sequential = { ratings }

        assertEquals(ratings, useCase(ids))
        assertEquals(listOf(ids), repository.sequentialIds)
        assertTrue(repository.parallelIds.isEmpty())
    }

    @Test
    fun acceptsEmptyIdsAndEmptyResult() = runTest {
        repository.sequential = { emptyMap() }

        assertEquals(emptyMap<String, Float>(), useCase(emptyList()))
        assertEquals(listOf(emptyList<String>()), repository.sequentialIds)
    }

    @Test
    fun repeatedInvocationLoadsUpdatedRatings() = runTest {
        repository.sequential = { mapOf("app" to 3f) }
        assertEquals(mapOf("app" to 3f), useCase(listOf("app")))
        repository.sequential = { mapOf("app" to 5f) }

        assertEquals(mapOf("app" to 5f), useCase(listOf("app")))
        assertEquals(listOf(listOf("app"), listOf("app")), repository.sequentialIds)
    }

    @Test
    fun propagatesFailureWithoutRetryOrSwitchingMode() = runTest {
        val failure = IOException("Ratings unavailable")
        repository.sequential = { throw failure }

        assertSame(failure, assertSuspendThrows<IOException> { useCase(listOf("app")) })
        assertEquals(listOf(listOf("app")), repository.sequentialIds)
        assertTrue(repository.parallelIds.isEmpty())
    }

    @Test
    fun propagatesCancellation() = runTest {
        val cancellation = CancellationException("Cancelled")
        repository.sequential = { throw cancellation }

        assertSame(cancellation, assertSuspendThrows<CancellationException> { useCase(listOf("app")) })
    }
}
