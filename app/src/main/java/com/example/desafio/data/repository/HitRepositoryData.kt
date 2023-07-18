package com.example.desafio.data.repository

import com.example.desafio.data.datasource.HitRemoteDataSource
import com.example.desafio.domain.HitDomain
import com.example.desafio.domain.repository.HitRepository
import javax.inject.Inject

class HitRepositoryData @Inject constructor(
    private val remoteDataSource: HitRemoteDataSource
) : HitRepository {
    override suspend fun getHits(): Result<List<HitDomain>> {
        return remoteDataSource.getHits()
    }

}