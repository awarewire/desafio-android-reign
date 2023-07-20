package com.example.desafio.domain

import com.example.desafio.domain.commons.ObservableUseCase
import com.example.desafio.domain.repository.HitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubscribeHitsUseCase @Inject constructor(
    private val repository: HitRepository
) : ObservableUseCase<List<HitDomain>, Unit?> {

    override fun run(params: Unit?): Flow<Result<List<HitDomain>>> {
        return repository.getHitsStream()
    }

}