package com.vgb3.vke.domain.repository

import com.vgb3.vke.domain.model.App
import com.vgb3.vke.domain.model.AppDetails
import com.vgb3.vke.domain.model.SimilarApp


interface AppRepository {
    suspend fun getApps(): List<App>
    suspend fun getAppDetails(id: String): AppDetails
    suspend fun getSimilarApps(id: String): List<SimilarApp>
    suspend fun loadRatingsSequentially(ids: List<String>): Map<String, Float>
    suspend fun loadRatingsInParallel(ids: List<String>): Map<String, Float>
}
