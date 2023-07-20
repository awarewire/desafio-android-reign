package com.example.desafio.presentation.hits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafio.domain.SubscribeHitsUseCase
import com.example.desafio.domain.SyncHitsUseCase
import com.example.desafio.domain.commons.DefaultUseCaseExecutor
import com.example.desafio.domain.commons.UseCaseExecutor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HitsViewModel @Inject constructor(
    private val subscribeHitsUseCase: SubscribeHitsUseCase,
    private val syncHitsUseCase: SyncHitsUseCase
) : ViewModel(), UseCaseExecutor by DefaultUseCaseExecutor() {

    private val _stateRefresh = MutableStateFlow(false)
    val stateRefresh: StateFlow<Boolean> = _stateRefresh
    private val _uiState = MutableStateFlow<MainStateUi>(MainStateUi.Initial)
    val uiState: StateFlow<MainStateUi> = _uiState

    init {
        refreshData()
        subscribeHits()
    }

    private fun subscribeHits() {
        subscribeHitsUseCase(viewModelScope, Unit) { either ->
            either.onSuccess { hitsDomain ->
                _uiState.value = MainStateUi.DisplayHits(hitsDomain.toListStateUi())
            }.onFailure {
                _uiState.value = MainStateUi.ErrorNetwork
            }
        }
    }

    fun refreshData() {
        _stateRefresh.value = true
        syncHitsUseCase(viewModelScope, Unit) {
            _stateRefresh.value = false
            Timber.d("result: $it")
        }
    }
}