package com.vgb3.vke.data

import android.annotation.SuppressLint
import com.vgb3.vke.R
import com.vgb3.vke.presentation.common.model.AppItem
import kotlinx.coroutines.delay

class AppRepository {

    suspend fun getApps(): List<AppItem> {
        delay(LOADING_DELAY_MS)
        return mockApps
    }

    private companion object {
        const val LOADING_DELAY_MS = 1_000L
    }
}

@SuppressLint("ResourceType")
val mockApps = listOf(
    AppItem(
        id = 1,
        picture = "https://static.rustore.ru/imgproxy/jinnNvWDiuYKlR6rYbP9DedPzELHgbe8opPzXh8k8FE/preset:web_app_icon_62/plain/https://static.rustore.ru/apk/220863/content/ICON/2238e3ca-e3e7-41d0-b037-777ddc637a5b.png@webp",
        title = "Т-Банк",
        description = "Управляйте финансами в приложении...",
        pictureRes = R.drawable.tbank
    ),
    AppItem(
        id = 2,
        picture = "https://static.rustore.ru/imgproxy/APsbtHxkVa4MZ0DXjnIkSwFQ_KVIcqHK9o3gHY6pvOQ/preset:web_app_icon_62/plain/https://static.rustore.ru/apk/393868735/content/ICON/3f605e3e-f5b3-434c-af4d-77bc5f38820e.png@webp",
        title = "Гильдия Героев",
        description = "Экшен ММО РПГ: собирайте отряд и сражайтесь с боссами",
        pictureRes = R.drawable.tbank
    ),
    // Пустая ссылка: здесь отработает fallback — локальная картинка из pictureRes
    AppItem(
        id = 3,
        picture = "",
        title = "Т-Банк",
        description = "Управляйте финансами в приложении...",
        pictureRes = R.drawable.tbank
    )
)
