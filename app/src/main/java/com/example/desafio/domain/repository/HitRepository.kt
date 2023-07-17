package com.example.desafio.domain.repository

import com.example.desafio.domain.HitDomain

interface HitRepository {
    fun getHits(): Result<List<HitDomain>>
}