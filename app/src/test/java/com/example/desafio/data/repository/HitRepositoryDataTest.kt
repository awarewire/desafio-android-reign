package com.example.desafio.data.repository

import com.example.desafio.data.datasource.HitRemoteDataSource
import com.example.desafio.data.datasource.remote.model.toListDomain
import com.example.desafio.utils.FakeData
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HitRepositoryDataTest {

    @Mock
    lateinit var mockHitRemoteDataSource: HitRemoteDataSource

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

}