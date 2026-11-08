package com.vgb3.vke.data.network

import com.vgb3.vke.data.dto.AppDetailsDto
import com.vgb3.vke.data.dto.AppDto
import retrofit2.http.GET
import retrofit2.http.Path

interface CatalogApi {

    @GET("catalog")
    suspend fun getApps(): List<AppDto>

    // Маршрут из задания. На учебном сервере пока возвращает HTTP 404.
    @GET("{id}")
    suspend fun getAppDetails(@Path("id") id: String): AppDetailsDto

    // Рабочий маршрут учебного сервера, используемый экраном карточки.
    @GET("catalog/{id}")
    suspend fun getCatalogAppDetails(@Path("id") id: String): AppDetailsDto
}
