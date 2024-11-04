package io.suppline.presentation.broadcastReceiver

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.suppline.domain.preferences.Preferences

@EntryPoint
@InstallIn(SingletonComponent::class)
interface BootReceiverEntryPoint {
    fun sharedPreferences(): Preferences
}