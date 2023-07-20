package com.example.desafio.domain

import com.example.desafio.domain.commons.UseCase
import com.example.desafio.domain.repository.HitRepository
import javax.inject.Inject

class SyncHitsUseCase @Inject constructor(
    private val repository: HitRepository
) : UseCase<Unit, Unit> {

    override suspend fun run(params: Unit): Result<Unit> {
        return repository.getHits().map { Unit }
    }
}