package com.example.desafio.presentation.hits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    //private val subscribeHitsUseCase: SubscribeHitsUseCase,
    private val syncHitsUseCase: SyncHitsUseCase,
    //private val deleteHitUseCase: DeleteHitUseCase
) : ViewModel(), UseCaseExecutor by DefaultUseCaseExecutor() {

    private val _uiState = MutableStateFlow<MainStateUi>(MainStateUi.Initial)
    val uiState: StateFlow<MainStateUi> = _uiState

    init {
        refreshData()
    }

    fun refreshData() {
        _uiState.value = MainStateUi.Loading
        syncHitsUseCase(viewModelScope, Unit) { result ->
            result.onSuccess { items ->
                _uiState.value = MainStateUi.DisplayHits(hist = items.toListStateUi())
            }.onFailure {
                Timber.d("error $it")
                _uiState.value = MainStateUi.ErrorNetwork
            }
        }
    }
}