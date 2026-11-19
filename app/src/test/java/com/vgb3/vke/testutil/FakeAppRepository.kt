package com.vgb3.vke.testutil

import com.vgb3.vke.domain.model.App
import com.vgb3.vke.domain.model.AppDetails
import com.vgb3.vke.domain.model.SimilarApp
import com.vgb3.vke.domain.repository.AppRepository

internal class FakeAppRepository : AppRepository {
    var apps: suspend () -> List<App> = { error("Unexpected getApps") }
    var details: suspend (String) -> AppDetails = { error("Unexpected getAppDetails") }
    var similar: suspend (String) -> List<SimilarApp> = { error("Unexpected getSimilarApps") }
    var sequential: suspend (List<String>) -> Map<String, Float> = { error("Unexpected sequential load") }
    var parallel: suspend (List<String>) -> Map<String, Float> = { error("Unexpected parallel load") }
    var appsCalls = 0
        private set
    val detailsIds = mutableListOf<String>()
    val similarIds = mutableListOf<String>()
    val sequentialIds = mutableListOf<List<String>>()
    val parallelIds = mutableListOf<List<String>>()

    override suspend fun getApps(): List<App> {
        appsCalls++
        return apps()
    }

    override suspend fun getAppDetails(id: String): AppDetails {
        detailsIds += id
        return details(id)
    }

    override suspend fun getSimilarApps(id: String): List<SimilarApp> {
        similarIds += id
        return similar(id)
    }

    override suspend fun loadRatingsSequentially(ids: List<String>): Map<String, Float> {
        sequentialIds += ids.toList()
        return sequential(ids)
    }

    override suspend fun loadRatingsInParallel(ids: List<String>): Map<String, Float> {
        parallelIds += ids.toList()
        return parallel(ids)
    }
}
