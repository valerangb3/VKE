package com.vgb3.vke.domain.usecase

import com.vgb3.vke.domain.repository.AppRepository
import javax.inject.Inject

class LoadRatingsInParallelUseCase @Inject constructor(private val repository: AppRepository) {
    suspend operator fun invoke(ids: List<String>): Map<String, Float> =
        repository.loadRatingsInParallel(ids)
}
