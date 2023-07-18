package com.example.desafio.domain.repository

import com.example.desafio.domain.HitDomain
import kotlinx.coroutines.flow.Flow

interface HitRepository {
    suspend fun getHits(): Result<List<HitDomain>>

    fun getHitsStream(): Flow<Result<List<HitDomain>>>
}