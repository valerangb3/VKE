package com.vgb3.vke.data.mapper

import com.vgb3.vke.data.dto.AppDetailsDto
import com.vgb3.vke.data.dto.AppDto
import com.vgb3.vke.data.dto.SimilarAppDto
import com.vgb3.vke.domain.model.App
import com.vgb3.vke.domain.model.AppDetails
import com.vgb3.vke.domain.model.SimilarApp
import kotlin.math.roundToInt


fun AppDto.toDomain(): App = App(
    id = id,
    title = appName,
    description = shortDescription.orEmpty(),
    iconUrl = iconUrl.orEmpty(),
)

fun AppDetailsDto.toDomain(): AppDetails = AppDetails(
    id = id,
    name = appName,
    developer = companyName,
    category = categoryName,
    age = ageLimit,
    appSize = fileSizeBytes.toMegabytes(),
    screenshotItems = screenshotUrls.orEmpty(),
    description = fullDescription.orEmpty(),
    iconApp = iconUrl.orEmpty(),
)

fun SimilarAppDto.toDomain(): SimilarApp = SimilarApp(
    id = packageName,
    name = appName,
    iconUrl = iconUrl.orEmpty(),
)

private const val BYTES_IN_MEGABYTE = 1024.0 * 1024.0

private fun Long.toMegabytes(): Double = (this / BYTES_IN_MEGABYTE * 10).roundToInt() / 10.0
