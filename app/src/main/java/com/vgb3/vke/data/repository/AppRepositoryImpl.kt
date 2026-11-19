package com.vgb3.vke.data.repository

import com.vgb3.vke.data.mapper.toDomain
import com.vgb3.vke.data.network.CatalogApi
import com.vgb3.vke.domain.model.App
import com.vgb3.vke.domain.model.AppDetails
import com.vgb3.vke.domain.model.SimilarApp
import com.vgb3.vke.domain.repository.AppRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val api: CatalogApi,
) : AppRepository {

    override suspend fun getApps(): List<App> = api.getApps().map { it.toDomain() }

    override suspend fun getAppDetails(id: String): AppDetails =
        api.getCatalogAppDetails(id).toDomain()

    override suspend fun getSimilarApps(id: String): List<SimilarApp> = getApps()
        .filter { it.id != id }
        .map { SimilarApp(id = it.id, name = it.title, iconUrl = it.iconUrl) }

    override suspend fun loadRatingsSequentially(ids: List<String>): Map<String, Float> = buildMap {
        for (id in ids) {
            put(id, api.getRating(id))
        }
    }

    override suspend fun loadRatingsInParallel(ids: List<String>): Map<String, Float> = coroutineScope {
        ids.map { id ->
            async { id to api.getRating(id) }
        }.awaitAll().toMap()
    }
}
