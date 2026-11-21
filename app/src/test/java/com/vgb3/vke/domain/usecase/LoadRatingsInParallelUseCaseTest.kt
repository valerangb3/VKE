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

class LoadRatingsInParallelUseCaseTest {
    private val repository = FakeAppRepository()
    private val useCase = LoadRatingsInParallelUseCase(repository)

    @Test
    fun usesParallelMethodAndPreservesIdOrderAndDuplicates() = runTest {
        val ids = listOf("second", "first", "second")
        val ratings = mapOf("first" to 3.5f, "second" to 4.7f)
        repository.parallel = { ratings }

        assertEquals(ratings, useCase(ids))
        assertEquals(listOf(ids), repository.parallelIds)
        assertTrue(repository.sequentialIds.isEmpty())
    }

    @Test
    fun acceptsEmptyIdsAndEmptyResult() = runTest {
        repository.parallel = { emptyMap() }

        assertEquals(emptyMap<String, Float>(), useCase(emptyList()))
        assertEquals(listOf(emptyList<String>()), repository.parallelIds)
    }

    @Test
    fun repeatedInvocationLoadsUpdatedRatings() = runTest {
        repository.parallel = { mapOf("app" to 3f) }
        assertEquals(mapOf("app" to 3f), useCase(listOf("app")))
        repository.parallel = { mapOf("app" to 5f) }

        assertEquals(mapOf("app" to 5f), useCase(listOf("app")))
        assertEquals(listOf(listOf("app"), listOf("app")), repository.parallelIds)
    }

    @Test
    fun propagatesFailureWithoutRetryOrSwitchingMode() = runTest {
        val failure = IOException("Ratings unavailable")
        repository.parallel = { throw failure }

        assertSame(failure, assertSuspendThrows<IOException> { useCase(listOf("app")) })
        assertEquals(listOf(listOf("app")), repository.parallelIds)
        assertTrue(repository.sequentialIds.isEmpty())
    }

    @Test
    fun propagatesCancellation() = runTest {
        val cancellation = CancellationException("Cancelled")
        repository.parallel = { throw cancellation }

        assertSame(cancellation, assertSuspendThrows<CancellationException> { useCase(listOf("app")) })
    }
}
