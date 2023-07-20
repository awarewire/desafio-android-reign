@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.desafio.data.repository

import com.example.desafio.data.datasource.HitRemoteDataSource
import com.example.desafio.data.datasource.db.HitsDao
import com.example.desafio.data.datasource.db.toListEntity
import com.example.desafio.data.datasource.device.NetworkHandler
import com.example.desafio.data.datasource.remote.model.toListDomain
import com.example.desafio.domain.HitDomain
import com.example.desafio.domain.commons.errors.NetworkConnection
import com.example.desafio.utils.FakeData
import com.example.desafio.utils.FakeData.getFakeHitDomain
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HitRepositoryDataTest {

    @Mock
    lateinit var mockHitRemoteDataSource: HitRemoteDataSource

    @Mock
    lateinit var mockHitsDao: HitsDao

    @Mock
    lateinit var mockNetworkHandler: NetworkHandler

    @InjectMocks
    lateinit var sutHitRepositoryData: HitRepositoryData

    @Test
    fun `given hits when getHits then success Result returned`() {
        runTest {
            val hits = FakeData.getFakeHitResponseModel().hits!!.toListDomain()
            val response = Result.success(hits)
            whenever(mockHitRemoteDataSource.getHits()).thenReturn(response)

            val result = sutHitRepositoryData.getHits()

            Assert.assertEquals(result.isSuccess, true)
        }
    }

    @Test
    fun `given hits when getHitsStream then dao data equals result domain data`() =
        runTest {
            val hitsDomain = listOf(getFakeHitDomain(), getFakeHitDomain())
            whenever(mockHitsDao.getHitsStream()).thenReturn(
                flow { emit(hitsDomain.toListEntity()) }
            )

            val domainStream = sutHitRepositoryData.getHitsStream().first()
            assertEquals(Result.success(hitsDomain), domainStream)
        }

    @Test
    fun `given hits EMPTY when getHitsStream then error Result returned`() =
        runTest {
            val hitsDomain = listOf<HitDomain>()
            whenever(mockHitsDao.getHitsStream()).thenReturn(
                flow { emit(hitsDomain.toListEntity()) }
            )
            whenever(mockNetworkHandler.isConnected()).thenReturn(false)

            val domainStream = sutHitRepositoryData.getHitsStream().first()
            assertEquals(Result.failure<List<HitDomain>>(NetworkConnection()), domainStream)
        }

}