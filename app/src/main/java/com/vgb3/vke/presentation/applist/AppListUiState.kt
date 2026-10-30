package com.vgb3.vke.presentation.applist

import androidx.annotation.StringRes
import com.vgb3.vke.domain.model.App

sealed interface AppListUiState {
    data object Loading : AppListUiState
    data class Content(val apps: List<App>) : AppListUiState
}

sealed interface AppListEvent {
    data class ShowSnackbar(
        @StringRes val messageRes: Int,
        val formatArgs: List<String> = emptyList(),
    ) : AppListEvent
}
