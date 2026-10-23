package com.vgb3.vke.presentation.common.model

data class AppDetails(
    val name: String,
    val developer: String,
    val category: String,
    val age: Int,
    val appSize: Double,
    val screenshotItems: List<String>,
    val description: String,
    val iconApp: String,
)
