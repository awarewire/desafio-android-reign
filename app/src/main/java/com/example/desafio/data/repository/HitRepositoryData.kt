package com.example.desafio.data.repository

import com.example.desafio.domain.HitDomain
import com.example.desafio.domain.repository.HitRepository

class HitRepositoryData : HitRepository {
    override fun getHits(): Result<List<HitDomain>> {
        TODO("Not yet implemented")
    }

}