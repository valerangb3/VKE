package com.vgb3.vke.data.repository

import com.vgb3.vke.data.mapper.toDomain
import com.vgb3.vke.data.mock.MockAppData
import com.vgb3.vke.domain.model.App
import com.vgb3.vke.domain.model.AppDetails
import com.vgb3.vke.domain.model.SimilarApp
import com.vgb3.vke.domain.repository.AppRepository
import kotlinx.coroutines.delay


class AppRepositoryImpl : AppRepository {

    override suspend fun getApps(): List<App> {
        delay(LOADING_DELAY_MS) // имитация сетевого запроса
        return MockAppData.apps.map { it.toDomain() }
    }

    override suspend fun getAppDetails(id: Long): AppDetails {
        delay(LOADING_DELAY_MS)
        return MockAppData.appDetails(id).toDomain()
    }

    override suspend fun getSimilarApps(id: Long): List<SimilarApp> =
        MockAppData.similarApps.map { it.toDomain() }

    private companion object {
        const val LOADING_DELAY_MS = 1_000L
    }
}
