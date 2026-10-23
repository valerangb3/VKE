package com.vgb3.vke.presentation.applist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vgb3.vke.R
import com.vgb3.vke.data.AppRepository
import com.vgb3.vke.presentation.common.model.AppItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AppListViewModel(
    private val repository: AppRepository = AppRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<AppListUiState>(AppListUiState.Loading)
    val state: StateFlow<AppListUiState> = _state.asStateFlow()

    private val _events = Channel<AppListEvent>(Channel.BUFFERED)
    val events: Flow<AppListEvent> = _events.receiveAsFlow()

    init {
        loadApps()
    }

    fun onToolbarClick() {
        _events.trySend(AppListEvent.ShowSnackbar(R.string.app_list_toolbar_snackbar))
    }

    fun onInstallClick(app: AppItem) {
        _events.trySend(
            AppListEvent.ShowSnackbar(
                messageRes = R.string.app_list_install_snackbar,
                formatArgs = listOf(app.title),
            )
        )
    }

    private fun loadApps() {
        viewModelScope.launch {
            _state.value = AppListUiState.Content(repository.getApps())
        }
    }
}
