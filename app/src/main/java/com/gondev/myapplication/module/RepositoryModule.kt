package com.gondev.myapplication.module

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    /*@Binds
    @Singleton
    fun bindTokenRepository(tokenRepository: TokenRepository): TokenRepository*/
}