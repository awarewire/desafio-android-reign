package com.example.desafio.domain

import com.example.desafio.domain.commons.UseCase
import com.example.desafio.domain.repository.HitRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class SyncHitsUseCase @Inject constructor(
    private val repository: HitRepository
) : UseCase<List<HitDomain>, Unit> {

    override suspend fun run(params: Unit): Result<List<HitDomain>> {
        delay(2000L)
        throw Exception("ass")
        return repository.getHits()
    }
}