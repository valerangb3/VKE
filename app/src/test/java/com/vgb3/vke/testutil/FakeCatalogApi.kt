package com.vgb3.vke.testutil

import com.vgb3.vke.data.dto.AppDetailsDto
import com.vgb3.vke.data.dto.AppDto
import com.vgb3.vke.data.network.CatalogApi

internal class FakeCatalogApi : CatalogApi {
    var apps: suspend () -> List<AppDto> = { error("Unexpected getApps") }
    var details: suspend (String) -> AppDetailsDto = { error("Unexpected getCatalogAppDetails") }
    var appsCalls = 0
        private set
    val detailsIds = mutableListOf<String>()

    override suspend fun getApps(): List<AppDto> {
        appsCalls++
        return apps()
    }

    override suspend fun getCatalogAppDetails(id: String): AppDetailsDto {
        detailsIds += id
        return details(id)
    }

    override suspend fun getAppDetails(id: String): AppDetailsDto =
        error("Repository must use the working catalog/{id} route")

    override suspend fun getRating(id: String): Float = error("Unexpected getRating")
}
