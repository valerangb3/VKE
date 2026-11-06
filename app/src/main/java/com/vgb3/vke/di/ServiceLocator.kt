package com.vgb3.vke.di

import com.vgb3.vke.data.repository.AppRepositoryImpl
import com.vgb3.vke.domain.repository.AppRepository


object ServiceLocator {
    val appRepository: AppRepository by lazy { AppRepositoryImpl() }
}
