package com.vgb3.vke.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.vgb3.vke.presentation.applist.ListScreen
import com.vgb3.vke.presentation.details.DetailsScreen
import kotlinx.serialization.Serializable
import java.util.Map.entry

@Serializable
data object AppListKey : NavKey

@kotlinx.serialization.Serializable
data class AppDetailKey(val id: Long) : NavKey

// 2. Корневой composable
@Composable
fun AppNav() {
    val backStack = rememberNavBackStack(AppListKey)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<AppListKey> {
                ListScreen(
                    onItemClick = { id -> backStack.add(AppDetailKey(id)) }
                )
            }
            entry<AppDetailKey> { key ->
                DetailsScreen(appId = key.id)
            }
        }
    )
}