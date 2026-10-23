package com.vgb3.vke.navigation

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.vgb3.vke.presentation.applist.ListScreen
import com.vgb3.vke.presentation.details.DetailsScreen
import kotlinx.serialization.Serializable

@Serializable
data object AppListKey : NavKey

@Serializable
data class AppDetailKey(val id: Long) : NavKey

@Composable
fun AppNav() {
    val backStack = rememberNavBackStack(AppListKey)
    val activity = LocalActivity.current

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<AppListKey> {
                ListScreen(
                    onItemClick = { id -> backStack.add(AppDetailKey(id)) },
                    onBackClick = { activity?.finish() },
                )
            }
            entry<AppDetailKey> { key ->
                DetailsScreen(
                    appId = key.id,
                    onBackClick = { backStack.removeLastOrNull() },
                )
            }
        }
    )
}
