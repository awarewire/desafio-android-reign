package com.example.desafio.data.datasource

import com.example.desafio.domain.HitDomain

interface HitRemoteDataSource {
    suspend fun getHits(): Result<List<HitDomain>>
}