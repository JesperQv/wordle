package com.studioviolet.wordle.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal interface RepositoryModule {
    @Binds
    fun bindsWordleRepository(impl: WordleRepositoryImpl): WordleRepository
}
