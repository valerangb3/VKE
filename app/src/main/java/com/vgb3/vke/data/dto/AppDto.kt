package com.vgb3.vke.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AppDto(
    val id: String,
    val name: String,
    val description: String? = null,
    val category: String? = null,
    val iconUrl: String? = null,
)
