package com.vgb3.vke.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vgb3.vke.domain.repository.AppRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = DetailsViewModel.Factory::class)
class DetailsViewModel @AssistedInject constructor(
    @Assisted private val appId: String,
    private val repository: AppRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val state: StateFlow<DetailsUiState> = _state.asStateFlow()

    private var loadJob: Job? = null
    private var installJob: Job? = null

    init {
        loadDetails()
    }

    fun loadDetails() {
        if (loadJob?.isActive == true) return
        installJob?.cancel()
        installJob = null
        loadJob = viewModelScope.launch {
            _state.value = DetailsUiState.Loading
            val details = try {
                repository.getAppDetails(appId)
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _state.value = DetailsUiState.Error
                return@launch
            }

            val content = DetailsUiState.Content(details, emptyList())
            _state.value = content

            // Дополнительный каталог не задерживает показ карточки и не скрывает её при ошибке.
            try {
                val similarApps = repository.getSimilarApps(appId)
                _state.update { current ->
                    if (current is DetailsUiState.Content) {
                        current.copy(similarApps = similarApps)
                    } else {
                        current
                    }
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                // Карточка остаётся доступной без блока других приложений.
            }
        }
    }

    fun onInstallClick() {
        val current = _state.value
        if (current !is DetailsUiState.Content) return

        when (current.installState) {
            is InstallState.Installing -> {
                installJob?.cancel()
                installJob = null
                updateInstallState(InstallState.NotInstalled)
            }
            InstallState.NotInstalled -> {
                // Сразу меняем состояние: повторный тап отменяет даже ещё не запущенную корутину.
                updateInstallState(InstallState.Installing(0))
                installJob = viewModelScope.launch { emulateInstall() }
            }
            InstallState.Installed -> Unit
        }
    }

    private suspend fun emulateInstall() {
        for (progress in 5..100 step 5) {
            delay(150)
            updateInstallState(InstallState.Installing(progress))
        }
        updateInstallState(InstallState.Installed)
    }

    private fun updateInstallState(installState: InstallState) {
        _state.update { current ->
            if (current is DetailsUiState.Content) current.copy(installState = installState) else current
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(appId: String): DetailsViewModel
    }
}
