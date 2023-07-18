package com.example.desafio.data.datasource.remote

import com.example.desafio.data.datasource.HitRemoteDataSource
import com.example.desafio.data.datasource.device.NetworkHandler
import com.example.desafio.data.datasource.remote.model.toListDomain
import com.example.desafio.domain.HitDomain
import javax.inject.Inject

class RetrofitDataSource @Inject constructor(
    private val api: AppApi,
    override val networkHandler: NetworkHandler
) : ApiService(), HitRemoteDataSource {
    override suspend fun getHits(): Result<List<HitDomain>> {
        return request {
            api.getHits()
        }.map { model -> model.hits.orEmpty().toListDomain() }
    }

}