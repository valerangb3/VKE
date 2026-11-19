package com.vgb3.vke.data.repository

import com.vgb3.vke.data.dto.AppDetailsDto
import com.vgb3.vke.data.dto.AppDto
import com.vgb3.vke.data.network.CatalogApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.system.measureTimeMillis

class RatingsBenchmarkTest {
    @Test
    fun compareBothModesOnTwelveAppsWithRealDelays() = runBlocking {
        // Только каталог тестовый. getRating использует реализацию CatalogApi с реальным delay(1500).
        val api = object : CatalogApi {
            override suspend fun getApps(): List<AppDto> = List(12) {
                AppDto(id = "benchmark-${it + 1}", name = "Приложение ${it + 1}")
            }
            override suspend fun getAppDetails(id: String): AppDetailsDto = error("Not used")
            override suspend fun getCatalogAppDetails(id: String): AppDetailsDto = error("Not used")
        }
        val repository = AppRepositoryImpl(api)
        val ids = repository.getApps().map { it.id }
        val sequential: Map<String, Float>
        val sequentialMs = measureTimeMillis { sequential = repository.loadRatingsSequentially(ids) }
        val parallel: Map<String, Float>
        val parallelMs = measureTimeMillis { parallel = repository.loadRatingsInParallel(ids) }

        assertEquals(ids.toSet(), sequential.keys)
        assertEquals(ids.toSet(), parallel.keys)
        assertTrue((sequential.values + parallel.values).all { it in 3f..5f })
        println("RATINGS_BENCHMARK apps=${ids.size} sequentialMs=$sequentialMs parallelMs=$parallelMs")
    }
}
