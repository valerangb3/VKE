package com.vgb3.vke.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AppDetailsDto(
    val id: String,
    val name: String,
    val developer: String,
    val category: String,
    val ageRating: Int,
    val size: Double,
    val iconUrl: String? = null,
    val screenshotUrlList: List<String>? = null,
    val description: String? = null,
)
