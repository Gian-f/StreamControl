package com.br.streamcontrol.domain.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.br.streamcontrol.data.remote.infra.ApiServiceFactory
import com.br.streamcontrol.data.remote.service.LocationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {
    @Provides
    fun provideViewModelFactory(application: Application): ViewModelProvider.Factory {
        return ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    fun provideLocationService(): LocationService {
        return ApiServiceFactory.createLocationService()
    }
}