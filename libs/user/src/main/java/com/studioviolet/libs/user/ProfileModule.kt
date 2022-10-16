package com.studioviolet.libs.user

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal interface UserModule {
    @Binds
    fun binds(impl: UserRepositoryImpl): UserRepository
}