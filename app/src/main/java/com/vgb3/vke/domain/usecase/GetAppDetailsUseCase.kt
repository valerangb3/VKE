package com.vgb3.vke.domain.usecase

import com.vgb3.vke.domain.model.AppDetails
import com.vgb3.vke.domain.repository.AppRepository
import javax.inject.Inject

class GetAppDetailsUseCase @Inject constructor(private val repository: AppRepository) {
    suspend operator fun invoke(id: String): AppDetails = repository.getAppDetails(id)
}
