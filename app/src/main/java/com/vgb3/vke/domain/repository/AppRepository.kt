package com.vgb3.vke.domain.repository

import com.vgb3.vke.domain.model.App
import com.vgb3.vke.domain.model.AppDetails
import com.vgb3.vke.domain.model.SimilarApp


interface AppRepository {
    suspend fun getApps(): List<App>
    suspend fun getAppDetails(id: Long): AppDetails
    suspend fun getSimilarApps(id: Long): List<SimilarApp>
}
