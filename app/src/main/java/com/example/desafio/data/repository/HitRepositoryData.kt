package com.example.desafio.data.repository

import com.example.desafio.data.datasource.HitRemoteDataSource
import com.example.desafio.data.datasource.db.HitsDao
import com.example.desafio.data.datasource.db.toListDomain
import com.example.desafio.data.datasource.db.toListEntity
import com.example.desafio.data.datasource.device.NetworkHandler
import com.example.desafio.domain.HitDomain
import com.example.desafio.domain.commons.errors.NetworkConnection
import com.example.desafio.domain.repository.HitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class HitRepositoryData @Inject constructor(
    private val remoteDataSource: HitRemoteDataSource,
    private val hitsDao: HitsDao,
    private val networkHandler: NetworkHandler
) : HitRepository {
    override suspend fun getHits(): Result<List<HitDomain>> {
        return remoteDataSource.getHits().onSuccess { hitsDomain ->
            runBlocking { saveHits(hitsDomain) }
        }
    }

    private suspend fun saveHits(hitsDomain: List<HitDomain>) {
        val hitsUpdate = hitsDao.getAllIdHitsDeleted()
        val insertHits = hitsDomain.filterNot { item -> hitsUpdate.contains(item.id) }
        hitsDao.insert(insertHits.toListEntity())
    }

    override fun getHitsStream(): Flow<Result<List<HitDomain>>> {
        return hitsDao.getHitsStream().map { entities ->
            if (entities.isEmpty() && !networkHandler.isConnected()) {
                return@map Result.failure<List<HitDomain>>(NetworkConnection())
            }
            return@map Result.success(entities.toListDomain())
        }
    }

    override suspend fun deleteHit(id: String): Result<Unit> {
        hitsDao.updateStatus(id)
        return Result.success(Unit)
    }

}