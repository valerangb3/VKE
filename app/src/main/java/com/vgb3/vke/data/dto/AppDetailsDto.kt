package com.vgb3.vke.data.dto

data class AppDetailsDto(
    val id: Long,
    val appName: String,
    val companyName: String,
    val categoryName: String,
    val ageLimit: Int,
    val fileSizeBytes: Long,
    val iconUrl: String?,
    val screenshotUrls: List<String>?,
    val fullDescription: String?,
)
