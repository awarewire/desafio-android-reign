package com.example.desafio.domain

import com.example.desafio.domain.commons.ServerError
import com.example.desafio.domain.repository.HitRepository
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SyncHitsUseCaseTest {

    @Mock
    lateinit var mockHitRepository: HitRepository

    @InjectMocks
    lateinit var sutSyncHitsUseCase: SyncHitsUseCase

    @Test
    fun `given hits when getHits then success Result returned`() {
        runTest {
            val hitDomains = listOf<HitDomain>()
            val response = Result.success(hitDomains)
            whenever(mockHitRepository.getHits()).thenReturn(response)

            val result = sutSyncHitsUseCase.run(Unit)

            Assert.assertEquals(result.isSuccess, true)
            verify(mockHitRepository).getHits()
        }
    }

    @Test
    fun `given hits when getHits then error Result returned`() {
        runTest {
            val response = Result.failure<List<HitDomain>>(ServerError())
            whenever(mockHitRepository.getHits()).thenReturn(response)

            val result = sutSyncHitsUseCase.run(Unit)

            Assert.assertEquals(result.isFailure, true)
            verify(mockHitRepository).getHits()
        }
    }
}