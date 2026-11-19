package com.vgb3.vke.presentation.applist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vgb3.vke.R
import com.vgb3.vke.domain.model.App
import com.vgb3.vke.domain.usecase.GetAppsUseCase
import com.vgb3.vke.domain.usecase.LoadRatingsInParallelUseCase
import com.vgb3.vke.domain.usecase.LoadRatingsSequentiallyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis
import javax.inject.Inject

@HiltViewModel
class AppListViewModel @Inject constructor(
    private val getApps: GetAppsUseCase,
    private val loadSequentialRatings: LoadRatingsSequentiallyUseCase,
    private val loadParallelRatings: LoadRatingsInParallelUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<AppListUiState>(AppListUiState.Loading)
    val state: StateFlow<AppListUiState> = _state.asStateFlow()

    private val _events = Channel<AppListEvent>(Channel.BUFFERED)
    val events: Flow<AppListEvent> = _events.receiveAsFlow()

    private var loadJob: Job? = null
    private var ratingsJob: Job? = null

    init {
        loadApps()
    }

    fun onToolbarClick() {
        _events.trySend(AppListEvent.ShowSnackbar(R.string.app_list_toolbar_snackbar))
    }

    fun onInstallClick(app: App) {
        _events.trySend(
            AppListEvent.ShowSnackbar(
                messageRes = R.string.app_list_install_snackbar,
                formatArgs = listOf(app.title),
            )
        )
    }

    fun loadApps() {
        if (loadJob?.isActive == true) return
        ratingsJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.value = AppListUiState.Loading
            try {
                _state.value = AppListUiState.Content(getApps())
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _state.value = AppListUiState.Error
            }
        }
    }

    fun loadRatingsSequentially() = loadRatings(parallel = false)

    fun loadRatingsInParallel() = loadRatings(parallel = true)

    private fun loadRatings(parallel: Boolean) {
        val current = _state.value as? AppListUiState.Content ?: return
        if (current.ratingsLoading || current.apps.isEmpty()) return
        val ids = current.apps.map { it.id }
        _state.value = current.copy(ratingsLoading = true, ratingsError = false)

        ratingsJob = viewModelScope.launch {
            try {
                val ratings: Map<String, Float>
                val duration = measureTimeMillis {
                    ratings = if (parallel) {
                        loadParallelRatings(ids)
                    } else {
                        loadSequentialRatings(ids)
                    }
                }
                _state.update { state ->
                    if (state is AppListUiState.Content) {
                        state.copy(
                            ratings = ratings,
                            ratingsLoading = false,
                            sequentialDurationMs = if (parallel) state.sequentialDurationMs else duration,
                            parallelDurationMs = if (parallel) duration else state.parallelDurationMs,
                        )
                    } else state
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _state.update { state ->
                    if (state is AppListUiState.Content) {
                        state.copy(ratingsLoading = false, ratingsError = true)
                    } else state
                }
            }
        }
    }
}
