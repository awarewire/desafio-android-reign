package com.example.desafio.domain

import com.example.desafio.domain.commons.UseCase
import com.example.desafio.domain.repository.HitRepository
import javax.inject.Inject

class DeleteHitUseCase @Inject constructor(
    private val repository: HitRepository
) : UseCase<Unit, String> {
    override suspend fun run(params: String): Result<Unit> {
        return repository.deleteHit(params)
    }

}