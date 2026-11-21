package com.vgb3.vke.domain.usecase

import com.vgb3.vke.domain.model.SimilarApp
import com.vgb3.vke.domain.repository.AppRepository
import javax.inject.Inject

class GetSimilarAppsUseCase @Inject constructor(private val repository: AppRepository) {
    suspend operator fun invoke(id: String): List<SimilarApp> = repository.getSimilarApps(id)
}
