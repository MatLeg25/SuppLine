package io.suppline.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.suppline.domain.preferences.Preferences
import io.suppline.domain.useCase.GetDailySupplementationUseCase
import io.suppline.domain.useCase.SaveDailySupplementationUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCases {

    @Provides
    @Singleton
    fun provideSaveDailySupplementationUseCase(preferences: Preferences): SaveDailySupplementationUseCase {
        return SaveDailySupplementationUseCase(preferences)
    }

    @Provides
    @Singleton
    fun provideGetDailySupplementationUseCase(preferences: Preferences): GetDailySupplementationUseCase {
        return GetDailySupplementationUseCase(preferences)
    }

}