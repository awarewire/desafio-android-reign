package com.example.desafio.presentation.di

import com.example.desafio.data.datasource.HitRemoteDataSource
import com.example.desafio.data.datasource.remote.RetrofitDataSource
import com.example.desafio.data.repository.HitRepositoryData
import com.example.desafio.domain.repository.HitRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun provideHitRepository(repository: HitRepositoryData): HitRepository


    @Binds
    abstract fun provideHitDataSource(datasource: RetrofitDataSource): HitRemoteDataSource
}