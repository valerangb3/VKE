package com.vgb3.vke.presentation.applist

import androidx.annotation.StringRes
import com.vgb3.vke.presentation.common.model.AppItem

sealed interface AppListUiState {
    data object Loading : AppListUiState
    data class Content(val apps: List<AppItem>) : AppListUiState
}

sealed interface AppListEvent {
    data class ShowSnackbar(
        @StringRes val messageRes: Int,
        val formatArgs: List<String> = emptyList(),
    ) : AppListEvent
}
