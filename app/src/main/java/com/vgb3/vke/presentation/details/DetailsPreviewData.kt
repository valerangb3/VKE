package com.vgb3.vke.presentation.details

import com.vgb3.vke.domain.model.AppDetails
import com.vgb3.vke.domain.model.SimilarApp

internal val previewAppDetails = AppDetails(
    id = 1,
    name = "Т-Банк",
    developer = "T-Bank",
    category = "Финансы",
    age = 0,
    appSize = 232.3,
    screenshotItems = List(4) { "" },
    description = "Т-Банк — приложение для управления деньгами и картами онлайн.\n\n" +
        "Оплачивайте ЖКХ, штрафы и госуслуги без комиссии, переводите через СБП.",
    iconApp = "",
)

internal val previewSimilarApps = List(5) { index ->
    SimilarApp(id = "similar-app-$index", name = "Т-Банк", iconUrl = "")
}
