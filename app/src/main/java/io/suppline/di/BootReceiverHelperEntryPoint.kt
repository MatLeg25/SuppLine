package io.suppline.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.suppline.presentation.broadcastReceiver.BootReceiverHelper

@EntryPoint
@InstallIn(SingletonComponent::class)
interface BootReceiverHelperEntryPoint {
    fun getBootReceiverHelper(): BootReceiverHelper
}