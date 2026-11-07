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
