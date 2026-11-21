package com.vgb3.vke.testutil

import com.vgb3.vke.data.dto.AppDetailsDto
import com.vgb3.vke.data.dto.AppDto
import com.vgb3.vke.domain.model.App
import com.vgb3.vke.domain.model.AppDetails

internal object TestData {
    val appDto = AppDto(
        id = "quick-note", name = "QuickNote", description = "Заметки",
        category = "Производительность", iconUrl = "https://example.com/icon.png",
    )
    val app = App(
        id = "quick-note", title = "QuickNote", description = "Заметки",
        iconUrl = "https://example.com/icon.png",
    )
    val detailsDto = AppDetailsDto(
        id = "quick-note", name = "QuickNote", developer = "QuickSoft",
        category = "Производительность", ageRating = 3, size = 8.2,
        iconUrl = "https://example.com/icon.png",
        screenshotUrlList = listOf("https://example.com/first.png", "https://example.com/second.png"),
        description = "Полное описание приложения",
    )
    val details = AppDetails(
        id = "quick-note", name = "QuickNote", developer = "QuickSoft",
        category = "Производительность", age = 3, appSize = 8.2,
        iconApp = "https://example.com/icon.png",
        screenshotItems = listOf("https://example.com/first.png", "https://example.com/second.png"),
        description = "Полное описание приложения",
    )
}
