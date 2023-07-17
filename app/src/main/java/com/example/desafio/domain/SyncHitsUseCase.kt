package com.example.desafio.domain

import com.example.desafio.domain.repository.HitRepository
import javax.inject.Inject

class SyncHitsUseCase @Inject constructor(
    private val repository: HitRepository
) {

    suspend fun run(): Result<Unit> {
        return repository.getHits().map { }
    }
}