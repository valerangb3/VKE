package com.vgb3.vke.data.repository

import com.vgb3.vke.data.dto.AppDetailsDto
import com.vgb3.vke.data.dto.AppDto
import com.vgb3.vke.data.network.CatalogApi
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RatingsRepositoryTest {
    private val ids = List(12) { "app-${it + 1}" }

    @Test
    fun sequentialRequestsTakeTwelveDelaysWithOnlyOneActiveRequest() = runTest {
        val api = TrackingApi()
        val started = testScheduler.currentTime
        val ratings = AppRepositoryImpl(api).loadRatingsSequentially(ids)

        assertEquals(18_000L, testScheduler.currentTime - started)
        assertEquals(ids, api.requested)
        assertEquals(1, api.maxActive)
        assertEquals(ids.associateWith { 4f }, ratings)
    }

    @Test
    fun parallelRequestsStartTogetherAndOnlyTakeOneDelay() = runTest {
        val api = TrackingApi()
        val started = testScheduler.currentTime
        val ratings = AppRepositoryImpl(api).loadRatingsInParallel(ids)

        assertEquals(1500L, testScheduler.currentTime - started)
        assertEquals(12, api.maxActive)
        assertEquals(ids.toSet(), ratings.keys)
        assertTrue(ratings.values.all { it == 4f })
        assertEquals(0, api.active)
    }

    @Test
    fun cancellingParentCancelsEveryParallelRequest() = runTest {
        val api = TrackingApi()
        val job = launch { AppRepositoryImpl(api).loadRatingsInParallel(ids) }
        runCurrent()
        assertEquals(12, api.active)

        job.cancelAndJoin()

        assertEquals(0, api.active)
        assertEquals(12, api.cancelled)
    }

    @Test
    fun oneFailedChildCancelsItsSiblingsAndPropagatesTheError() = runTest {
        val api = TrackingApi(failingId = ids.first())
        try {
            AppRepositoryImpl(api).loadRatingsInParallel(ids)
            error("Expected rating failure")
        } catch (_: IOException) {
            assertEquals(0, api.active)
            assertEquals(11, api.cancelled)
        }
    }

    @Test
    fun emptyListDoesNotStartAnyRequests() = runTest {
        val api = TrackingApi()
        val repository = AppRepositoryImpl(api)
        assertTrue(repository.loadRatingsSequentially(emptyList()).isEmpty())
        assertTrue(repository.loadRatingsInParallel(emptyList()).isEmpty())
        assertTrue(api.requested.isEmpty())
    }

    private class TrackingApi(private val failingId: String? = null) : CatalogApi {
        val requested = mutableListOf<String>()
        var active = 0
        var maxActive = 0
        var cancelled = 0

        override suspend fun getRating(id: String): Float {
            requested += id
            active++
            maxActive = maxOf(maxActive, active)
            try {
                if (id == failingId) {
                    delay(100)
                    throw IOException("Rating unavailable")
                }
                delay(1500)
                return 4f
            } finally {
                active--
                if (!currentCoroutineContext().isActive) cancelled++
            }
        }

        override suspend fun getApps(): List<AppDto> = error("Not used")
        override suspend fun getAppDetails(id: String): AppDetailsDto = error("Not used")
        override suspend fun getCatalogAppDetails(id: String): AppDetailsDto = error("Not used")
    }
}
