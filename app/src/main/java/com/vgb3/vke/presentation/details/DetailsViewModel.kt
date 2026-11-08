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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = DetailsViewModel.Factory::class)
class DetailsViewModel @AssistedInject constructor(
    @Assisted private val appId: String,
    private val repository: AppRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val state: StateFlow<DetailsUiState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadDetails()
    }

    fun loadDetails() {
        if (loadJob?.isActive == true) return
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
                _state.value = content.copy(similarApps = repository.getSimilarApps(appId))
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                // Карточка остаётся доступной без блока других приложений.
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(appId: String): DetailsViewModel
    }
}
