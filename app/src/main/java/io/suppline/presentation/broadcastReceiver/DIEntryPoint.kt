package io.suppline.presentation.broadcastReceiver

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.suppline.domain.preferences.Preferences
import io.suppline.domain.useCase.GetDailySupplementationUseCase
import io.suppline.domain.useCase.SaveDailySupplementationUseCase

@EntryPoint
@InstallIn(SingletonComponent::class)
interface BootReceiverEntryPoint {
    fun sharedPreferences(): Preferences
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface GetDailySupplementationUseCaseEntryPoint {
    fun getDailySupplementationUseCase(): GetDailySupplementationUseCase
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SaveDailySupplementationUseCaseEntryPoint {
    fun getSaveDailySupplementationUseCase(): SaveDailySupplementationUseCase
}