package com.vgb3.vke.domain.usecase

import com.vgb3.vke.testutil.FakeAppRepository
import com.vgb3.vke.testutil.TestData.details
import com.vgb3.vke.testutil.assertSuspendThrows
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetAppDetailsUseCaseTest {
    private val repository = FakeAppRepository()
    private val useCase = GetAppDetailsUseCase(repository)

    @Test
    fun returnsDetailsAndForwardsSelectedIdExactlyOnce() = runTest {
        repository.details = { details }

        assertEquals(details, useCase(details.id))
        assertEquals(listOf(details.id), repository.detailsIds)
    }

    @Test
    fun eachInvocationUsesItsOwnIdWithoutCachingPreviousDetails() = runTest {
        repository.details = { id -> details.copy(id = id) }

        assertEquals(details.copy(id = "first"), useCase("first"))
        assertEquals(details.copy(id = "second"), useCase("second"))
        assertEquals(listOf("first", "second"), repository.detailsIds)
    }

    @Test
    fun waitsForSuspendingRepositoryResult() = runTest {
        val pending = CompletableDeferred<com.vgb3.vke.domain.model.AppDetails>()
        repository.details = { pending.await() }
        val result = async { useCase(details.id) }
        runCurrent()

        assertFalse(result.isCompleted)
        assertEquals(listOf(details.id), repository.detailsIds)
        pending.complete(details)
        assertEquals(details, result.await())
    }

    @Test
    fun propagatesRepositoryFailureWithoutRetryOrFallback() = runTest {
        val failure = IOException("Details unavailable")
        repository.details = { throw failure }

        assertSame(failure, assertSuspendThrows<IOException> { useCase(details.id) })
        assertEquals(listOf(details.id), repository.detailsIds)
    }

    @Test
    fun propagatesCancellation() = runTest {
        val cancellation = CancellationException("Cancelled")
        repository.details = { throw cancellation }

        assertSame(cancellation, assertSuspendThrows<CancellationException> { useCase(details.id) })
    }
}
