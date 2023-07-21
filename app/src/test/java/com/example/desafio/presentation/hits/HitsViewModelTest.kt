@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.desafio.presentation.hits

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.desafio.domain.HitDomain
import com.example.desafio.domain.SubscribeHitsUseCase
import com.example.desafio.domain.SyncHitsUseCase
import com.example.desafio.utils.FakeData.getFakeHitDomain
import com.example.desafio.utils.MainDispatcherRule
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class HitsViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    lateinit var mockSubscribeHitsUseCase: SubscribeHitsUseCase

    @Mock
    lateinit var mockSyncHitsUseCase: SyncHitsUseCase

    @InjectMocks
    lateinit var sutHitsViewModel: HitsViewModel

    @Before
    fun setup() {
        sutHitsViewModel = HitsViewModel(
            mockSubscribeHitsUseCase,
            mockSyncHitsUseCase
        )
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when refreshData then mockSyncHitsUseCase is called`() {
        runTest {
            val response = Result.success(Unit)
            whenever(mockSyncHitsUseCase.run(Unit)).thenReturn(response)

            sutHitsViewModel.refreshData()

            advanceUntilIdle()
            verify(mockSyncHitsUseCase, times(3)).run(Unit)
        }
    }

    @Test
    fun `when subscribeHits then mockSubscribeHitsUseCase is called `() {
        runTest {
            val hitsDomain = listOf(getFakeHitDomain(), getFakeHitDomain())
            val mainStateUi = MainStateUi.DisplayHits(hitsDomain.toListStateUi())
            val response = flow { emit(Result.success(hitsDomain)) }
            whenever(mockSubscribeHitsUseCase.run(Unit)).thenReturn(response)
            val result = mutableListOf<List<HitDomain>?>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                mockSubscribeHitsUseCase.run(Unit).map { it.getOrNull() }.toList(result)
            }

            sutHitsViewModel.subscribeHits()

            advanceUntilIdle()
            val hitsUiState = sutHitsViewModel.uiState.value
            verify(mockSubscribeHitsUseCase, times(4)).run(Unit)
            Assert.assertEquals(mainStateUi, hitsUiState)
        }
    }
}