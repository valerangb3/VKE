package com.vgb3.vke.presentation.details

import com.vgb3.vke.domain.model.AppDetails
import com.vgb3.vke.domain.model.SimilarApp

sealed interface DetailsUiState {
    data object Loading : DetailsUiState
    data class Content(
        val details: AppDetails,
        val similarApps: List<SimilarApp>,
    ) : DetailsUiState
}
