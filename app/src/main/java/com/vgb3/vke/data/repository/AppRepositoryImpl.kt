package com.vgb3.vke.data.repository

import com.vgb3.vke.data.mapper.toDomain
import com.vgb3.vke.data.network.CatalogApi
import com.vgb3.vke.domain.model.App
import com.vgb3.vke.domain.model.AppDetails
import com.vgb3.vke.domain.model.SimilarApp
import com.vgb3.vke.domain.repository.AppRepository
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
}
