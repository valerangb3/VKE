package com.vgb3.vke.data.mapper

import com.vgb3.vke.data.dto.AppDetailsDto
import com.vgb3.vke.data.dto.AppDto
import com.vgb3.vke.domain.model.App
import com.vgb3.vke.domain.model.AppDetails


fun AppDto.toDomain(): App = App(
    id = id,
    title = name,
    description = description.orEmpty(),
    iconUrl = iconUrl.orEmpty(),
)

fun AppDetailsDto.toDomain(): AppDetails = AppDetails(
    id = id,
    name = name,
    developer = developer,
    category = category,
    age = ageRating,
    appSize = size,
    screenshotItems = screenshotUrlList.orEmpty(),
    description = description.orEmpty(),
    iconApp = iconUrl.orEmpty(),
)

// Категория есть только в DTO списка; Domain-модель App её не хранит.
fun App.toDto(): AppDto = AppDto(
    id = id,
    name = title,
    description = description,
    iconUrl = iconUrl,
)

fun AppDetails.toDto(): AppDetailsDto = AppDetailsDto(
    id = id,
    name = name,
    developer = developer,
    category = category,
    ageRating = age,
    size = appSize,
    iconUrl = iconApp,
    screenshotUrlList = screenshotItems,
    description = description,
)
