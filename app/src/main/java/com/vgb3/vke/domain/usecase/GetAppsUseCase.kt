package com.vgb3.vke.domain.usecase

import com.vgb3.vke.domain.model.App
import com.vgb3.vke.domain.repository.AppRepository
import javax.inject.Inject

class GetAppsUseCase @Inject constructor(private val repository: AppRepository) {
    suspend operator fun invoke(): List<App> = repository.getApps()
}
